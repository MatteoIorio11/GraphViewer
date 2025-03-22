package org.graphviewer.graph

import io.kotest.core.spec.style.DescribeSpec
import org.graphviewer.core.graph.GraphImpl
import org.junit.jupiter.api.assertThrows

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
    })
