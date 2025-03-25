package org.graphviewer.core.graph

/**
 * Interface for an Edge in the graph.
 */
interface Edge {
    /**
     * Returns the destination vertex of this edge
     */
    fun nextVertex(): Vertex
}
