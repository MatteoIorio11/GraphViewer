package org.graphviewer.core.graph

class GraphImpl : Graph {
    companion object {
        var separator: String = "->"

        fun create(edges: List<String> = listOf()): Graph {
            val graph = GraphImpl()
            edges
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .forEach { line ->
                    if (!line.contains(separator)) {
                        throw IllegalArgumentException("Invalid edge format")
                    }
                    val (v1, v2) = line.split(separator)
                    graph.addEdge(VertexImpl(v1.trim()), VertexImpl(v2.trim()))
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
