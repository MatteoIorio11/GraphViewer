package org.graphviewer.graph

import io.kotest.core.spec.style.DescribeSpec
import org.graphviewer.core.graph.GraphImpl
import org.graphviewer.core.graph.VertexImpl
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class GraphTest :
    DescribeSpec({
        describe("Given as input a list of edges It should be possible to create a graph") {
            val graph = GraphImpl.create(listOf("A->B", "B->C"))
            assert(graph.dump().size == 2)
        }
        describe("Given as input a list of edges that It is not well formatted It should throw an exception") {
            assertThrows<java.lang.IllegalArgumentException> {
                GraphImpl.create(listOf("A->B", "B"))
            }
        }
        describe("Given as input a list of edges, If we disable a vertex, It should not be present in the dump") {
            val graph = GraphImpl.create(listOf("A->B", "B->C", "C->D", "D->A"))
            graph.disableVertex(VertexImpl("A"))
            assertEquals(3, graph.dump().size)
        }
        describe("Given as input a list of edges, If we disable a vertex and the we enable It we should see It in the dump") {
            val graph = GraphImpl.create(listOf("A->B", "B->C", "C->D", "D->A"))
            graph.disableVertex(VertexImpl("A"))
            assertEquals(3, graph.dump().size)
            graph.enableVertex(VertexImpl("A"))
            assertEquals(4, graph.dump().size)
        }
        describe("Giving as input a list of edges It should be possible to get the list of all vertexs") {
            val graph = GraphImpl.create(listOf("A->B", "B->C", "C->D", "D->A", "D->E"))
            assertEquals(5, graph.vertexs().size)
        }
        describe("Giving as input a list of edges, If we disable a vertex, It should be present in the list of all vertexs") {
            val graph = GraphImpl.create(listOf("A->B", "B->C", "C->D", "D->A", "D->E"))
            graph.disableVertex(VertexImpl("A"))
            assertEquals(5, graph.vertexs().size)
        }
        describe("Giving as input a list of edges, If we disable a vertex, It should return false when ask If enable") {
            val graph = GraphImpl.create(listOf("A->B", "B->C", "C->D"))
            graph.disableVertex(VertexImpl("A"))
            assertFalse { graph.isVertexEnabled(VertexImpl("A")) }
        }
        describe("Giving a graph It should be possible to dump it into plant uml format") {
            val expected =
                """
                @startuml
                class a
                class b
                class c
                a --> b
                b --> c
                @enduml
                """.trimIndent()
            val graph = GraphImpl.create(listOf("a->b", "b->c"))
            assertEquals(expected, GraphImpl.toPlantUml(graph))
        }
    })
