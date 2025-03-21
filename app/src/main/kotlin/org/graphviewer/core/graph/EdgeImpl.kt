package org.graphviewer.core.graph

class EdgeImpl(private val next: Vertex): Edge {
    override fun nextVertex(): Vertex {
        return this.next
    }
}
