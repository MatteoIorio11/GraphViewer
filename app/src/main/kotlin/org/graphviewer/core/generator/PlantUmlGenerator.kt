package org.graphviewer.core.generator

import net.sourceforge.plantuml.SourceStringReader
import net.sourceforge.plantuml.security.SFile
import java.io.File

object PlantUmlGenerator {
    fun generateImage(content: String): File {
        val reader = SourceStringReader(content)
        val tempFile = File(".", "graph.png")
        tempFile.apply { delete() }
        if (tempFile.createNewFile()) {
            reader.outputImage(SFile.fromFile(tempFile))
        } else {
            throw IllegalStateException("Could not create temp file")
        }
        return tempFile
    }
}
