package org.graphviewer.core.graph

interface Graph {
    fun enableVertex(v: Vertex): Unit

    fun disableVertex(v: Vertex): Unit

    fun dump(): Map<Vertex, List<Edge>>

    fun vertexs(): Set<Vertex>

    fun isVertexEnabled(v: Vertex): Boolean

    fun addEdge(
        v1: Vertex,
        v2: Vertex,
    ): Unit
}
