@file:Suppress("ktlint:standard:no-wildcard-imports")

/**
 * GUI Generated using chat gpt-3, the input prompt was:
 * create a GUI in kotlin using (swing or whatever) structure in this way: the gut must be structured in three different portions,
 * the first one is where I will render an image so try to put It exactly in the center of the GUI,
 * then I want an area where it will be possible to insert text, and another area where it will be displayed a List of buttons,
 * and the list of buttons is dynamic so try to make it cool. I would like that the list of buttons is inside list item in this way
 * it will be possible to automatically put buttons inside it
 */

package org.graphviewer.core

import net.sourceforge.plantuml.SourceStringReader
import java.awt.*
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import javax.swing.*

class UMLGuiApp : JFrame("Graph Editor") {
    private val imageLabel = JLabel() // Label to display the UML image
    private val textArea = JTextArea(10, 30) // Text input area for UML code
    private val buttonListModel = DefaultListModel<JButton>() // Dynamic list model for buttons
    private val buttonList = JList(buttonListModel) // JList to hold buttons dynamically
    private val renderButton = JButton("Render Graph") // Button to generate UML image

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        preferredSize = Dimension(800, 600)

        // 📌 1. IMAGE PANEL (CENTER)
        val imagePanel = JPanel(BorderLayout())
        imagePanel.background = Color.LIGHT_GRAY
        imagePanel.add(imageLabel, BorderLayout.CENTER)
        add(imagePanel, BorderLayout.CENTER) // Set in the middle

        // 📌 2. TEXT INPUT AREA (BOTTOM LEFT)
        val inputPanel = JPanel(BorderLayout())
        inputPanel.border = BorderFactory.createTitledBorder("Enter Graph Code (Node -> Node)")
        inputPanel.add(JScrollPane(textArea), BorderLayout.CENTER)
        inputPanel.add(renderButton, BorderLayout.SOUTH) // Button to generate UML
        add(inputPanel, BorderLayout.SOUTH)

        // 📌 3. DYNAMIC BUTTON LIST (RIGHT SIDE)
        val buttonListPanel = JPanel(BorderLayout())
        buttonListPanel.border = BorderFactory.createTitledBorder("Vertexs")
        buttonList.fixedCellHeight = 40
        buttonList.cellRenderer = ButtonListRenderer()
        buttonList.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                buttonList.selectedValue?.doClick()
            }
        }
        buttonListPanel.add(JScrollPane(buttonList), BorderLayout.CENTER)
        add(buttonListPanel, BorderLayout.EAST)

        // 📌 BUTTON ACTIONS
        renderButton.addActionListener { generateUMLImage() }

        // Add some dynamic buttons
//        addDynamicButton("Example 1") { textArea.text = exampleUML }
//        addDynamicButton("Example 2") { textArea.text = exampleUML2 }

        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    // 🎨 FUNCTION TO GENERATE UML IMAGE FROM TEXT INPUT
    private fun generateUMLImage() {
        val umlCode = textArea.text.trim()
        if (umlCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Graph code!", "Error", JOptionPane.ERROR_MESSAGE)
            return
        }
        val image = generatePlantUMLImage(umlCode)
        imageLabel.icon = ImageIcon(image) // Display generated UML image
        pack() // Resize window to fit image
    }

    // 🖼️ FUNCTION TO GENERATE UML IMAGE IN MEMORY
    private fun generatePlantUMLImage(umlString: String): BufferedImage {
        val reader = SourceStringReader(umlString)
        val outputStream = ByteArrayOutputStream()
        reader.outputImage(outputStream).description // Generates image in memory
        return ImageIO.read(outputStream.toByteArray().inputStream())!!
    }

    // 🔄 FUNCTION TO ADD A DYNAMIC BUTTON
    private fun addDynamicButton(
        name: String,
        action: () -> Unit,
    ) {
        val button = JButton(name)
        button.addActionListener { action() }
        buttonListModel.addElement(button)
    }

    // 📌 BUTTON LIST RENDERER: Makes buttons inside JList
    class ButtonListRenderer : ListCellRenderer<JButton> {
        override fun getListCellRendererComponent(
            list: JList<out JButton>,
            value: JButton,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean,
        ): Component {
            value.background = if (isSelected) Color.LIGHT_GRAY else Color.WHITE
            return value
        }
    }

    private val exampleUML =
        """
        @startuml
        class Person {
            +name: String
            +age: Int
            +greet(): String
        }
        class Student {
            +studentId: Int
        }
        Person <|-- Student
        @enduml
        """.trimIndent()

    private val exampleUML2 =
        """
        @startuml
        Alice --> Bob
        Bob --> Charlie
        Charlie --> Alice
        @enduml
        """.trimIndent()
}

fun main() {
    SwingUtilities.invokeLater { UMLGuiApp() }
}
