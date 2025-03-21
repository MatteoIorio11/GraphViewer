package org.graphviewer.core

class VertexImpl(private val id: String): Vertex {
    override fun getId(): String {
        return id
    }
}
