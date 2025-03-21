package org.graphviewer.core

class EdgeImpl(private val next: Vertex):Edge {
    override fun nextVertex(): Vertex {
        return this.next
    }
}
