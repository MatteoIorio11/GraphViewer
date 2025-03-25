package org.graphviewer.core.graph

interface Graph {
    /**
     * Enable a vertex inside the graph. If the vertex is already enabled, this method does nothing.
     * @param v the vertex to enable
     */
    fun enableVertex(v: Vertex)

    /**
     * Disable a vertex inside the graph. If the vertex is already disabled, this method does nothing.
     * @param v the vertex to disable
     */
    fun disableVertex(v: Vertex)

    /**
     * Dump the input graph.
     * @return the graph as an adjacency list. The dump will be filtered by avoiding all the Vertex that are disabled.
     */
    fun dump(): Map<Vertex, List<Edge>>

    /**
     * Get all the vertex stored inside the graph.
     * @return the set of all the vertex in the graph. The set will contain all the vertex present in the graph,
     * independently if they are enabled or disabled.
     */
    fun vertexs(): Set<Vertex>

    /**
     * Check if the input vertex is enabled.
     * @param v the vertex to check
     * @return True if the vertex is enabled, False otherwise.
     */
    fun isVertexEnabled(v: Vertex): Boolean

    /**
     * Add a new edge to the graph, connecting the vertex v1 to the vertex v2.
     * @param v1 the source vertex
     * @param v2 the destination vertex
     */
    fun addEdge(
        v1: Vertex,
        v2: Vertex,
    )
}
