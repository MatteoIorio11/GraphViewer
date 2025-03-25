package org.graphviewer.core.graph

/**
 * Interface for a Vertex in the graph.
 */
interface Vertex {
    /**
     * Returns the ID of this vertex. Each vertex should be represented by an unique ID.
     */
    fun getId(): String
}
