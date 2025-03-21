package org.graphviewer.core.graph

class GraphImpl : Graph {
    companion object {
        fun create(edges: List<String>): Graph {
            val graph = GraphImpl()
            edges.forEach { line ->
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
        TODO("Not yet implemented")
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
