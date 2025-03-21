package org.graphviewer.core.graph

class VertexImpl(
    private val id: String,
) : Vertex {
    override fun getId(): String = id
}
