package org.graphviewer.core.graph

import com.google.common.base.Objects

class VertexImpl(
    private val id: String,
) : Vertex {
    override fun getId(): String = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherVertex = other as? VertexImpl ?: return false
        return id == otherVertex.id
    }

    override fun toString(): String = "Vertex: $id"

    override fun hashCode(): Int = Objects.hashCode(getId())
}
