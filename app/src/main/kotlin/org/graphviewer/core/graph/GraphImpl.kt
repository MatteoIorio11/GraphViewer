package org.graphviewer.core.graph

class GraphImpl : Graph {
    companion object {
        fun create(edges: List<String> = listOf()): Graph {
            val graph = GraphImpl()
            edges.forEach { line ->
                if (!line.contains("->")) {
                    throw IllegalArgumentException("Invalid edge format")
                }
                val (v1, v2) = line.split("->")
                graph.addEdge(VertexImpl(v1), VertexImpl(v2))
            }
            return graph
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
