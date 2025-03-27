package org.graphviewer.core.graph

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

object GraphUtils {
    /**
     * Check if the input list of lines is correctly formatted.
     * @param lines the list of lines to check
     * @return true if the input list is not empty and It is correctly formatted, false otherwise.
     */
    fun isValidFormat(lines: List<String>): Deferred<Boolean> =
        CoroutineScope(Dispatchers.IO).async {
            lines.isNotEmpty() &&
                lines
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .all { it.contains("->") && it.split("->").all { char -> char.isNotBlank() } }
        }

    /**
     * Convert the input graph to a PlantUML format.
     * @param graph the graph to dump into plantuml format
     * @return the graph formatted as a plantuml string.
     */
    fun toPlantUml(graph: Graph): String {
        val stringBuilder = StringBuilder()
        val adjacentList = graph.dump()
        stringBuilder.append("@startuml\n")
        val classes =
            (
                adjacentList.values
                    .flatten()
                    .map { it.nextVertex() }
                    .toSet() +
                    adjacentList.keys
            ).toList().sortedBy { it.getId() }
        classes.forEach { stringBuilder.append("class ${it.getId()}\n") }
        for (key in adjacentList.keys) {
            for (edge in adjacentList[key]!!) {
                stringBuilder.append("${key.getId()} --> ${edge.nextVertex().getId()}\n")
            }
        }
        stringBuilder.append("@enduml")
        return stringBuilder.toString()
    }
}
