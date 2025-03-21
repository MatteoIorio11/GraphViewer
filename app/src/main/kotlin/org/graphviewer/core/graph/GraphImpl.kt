package org.graphviewer.core.graph

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class GraphImpl : Graph {
    companion object {
        fun create(edges: List<String> = listOf()): Graph {
            val graph = GraphImpl()
            edges.forEach { line ->
                if (!line.contains("->")) {
                    throw IllegalArgumentException("Invalid edge format")
                }
                val (v1, v2) = line.split("->")
                graph.addEdge(VertexImpl(v1.trim()), VertexImpl(v2.trim()))
            }
            return graph
        }

        fun isValidFormat(lines: List<String>): Deferred<Boolean> =
            CoroutineScope(Dispatchers.IO).async {
                lines
                    .map { it -> it.trim() }
                    .all { it.contains("->") && it.split("->").all { char -> char.isNotBlank() } }
            }

        fun toPlantUml(graph: Graph): String {
            val stringBuilder = StringBuilder()
            val adjacentList = graph.dump()
            stringBuilder.append("@startuml\n")
            val classes =
                (
                    adjacentList.values
                        .flatten()
                        .map { it -> it.nextVertex() }
                        .toSet() +
                        adjacentList.keys
                ).toList().sortedBy { it -> it.getId() }
            classes.forEach { stringBuilder.append("class ${it.getId()}\n") }
            for (key in adjacentList.keys) {
                for (edge in adjacentList[key]!!) {
                    stringBuilder.append("${key.getId()} --> ${edge.nextVertex().getId()}\n")
                }
            }
            stringBuilder.append("@enduml")
            return stringBuilder.toString()
        }
    }

    private val adjacentList = mutableMapOf<Vertex, MutableList<Edge>>()
    private val disabledVertices = mutableSetOf<Vertex>()

    override fun enableVertex(v: Vertex) {
        disabledVertices.remove(v)
    }

    override fun disableVertex(v: Vertex) {
        disabledVertices.add(v)
    }

    override fun dump(): Map<Vertex, List<Edge>> {
        val result = mutableMapOf<Vertex, MutableList<Edge>>()
        adjacentList.keys
            .filter { !disabledVertices.contains(it) }
            .forEach {
                result[it] =
                    adjacentList[it]?.filter { e -> !disabledVertices.contains(e.nextVertex()) }?.toMutableList()!!
            }
        return result
    }

    override fun vertexs(): Set<Vertex> = adjacentList.keys + adjacentList.values.flatten().map { it.nextVertex() }

    override fun isVertexEnabled(v: Vertex): Boolean = !disabledVertices.contains(v)

    override fun addEdge(
        v1: Vertex,
        v2: Vertex,
    ) {
        if (!adjacentList.contains(v1)) {
            adjacentList[v1] = mutableListOf()
        }
        adjacentList[v1]?.add(EdgeImpl(v2))
    }
}
