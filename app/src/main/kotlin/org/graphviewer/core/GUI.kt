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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.sourceforge.plantuml.SourceStringReader
import org.graphviewer.core.graph.Graph
import org.graphviewer.core.graph.GraphImpl
import org.graphviewer.core.graph.VertexImpl
import java.awt.*
import java.awt.event.*
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import javax.swing.*

@OptIn(ExperimentalCoroutinesApi::class)
class GUIApp : JFrame("Graph Editor") {
    private val imageLabel = JLabel() // Label to display the UML image
    private val textArea = JTextArea(10, 30) // Text input area for UML code
    private val vertexButtonsPanel = JPanel() // Changed to use a panel with GridLayout instead of JList
    private val renderButton = JButton("Render Graph") // Button to generate UML image
    private var graph: Graph = GraphImpl()
    private var zoom = 1.0
    private var originalImage: Image? = null

    // Map to store buttons by vertex ID
    private val vertexButtons = mutableMapOf<String, JButton>()

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
        imageLabel.setHorizontalAlignment(JLabel.CENTER)
        imageLabel.setVerticalAlignment(JLabel.CENTER)
        inputPanel.add(renderButton, BorderLayout.SOUTH) // Button to generate UML
        add(inputPanel, BorderLayout.SOUTH)

        // 📌 3. DYNAMIC BUTTON LIST (RIGHT SIDE)
        val buttonListPanel = JPanel(BorderLayout())
        buttonListPanel.border = BorderFactory.createTitledBorder("Vertices")

        // Using a panel with a vertical BoxLayout for buttons instead of JList
        vertexButtonsPanel.layout = BoxLayout(vertexButtonsPanel, BoxLayout.Y_AXIS)
        buttonListPanel.add(JScrollPane(vertexButtonsPanel), BorderLayout.CENTER)
        add(buttonListPanel, BorderLayout.EAST)

        imagePanel.addMouseWheelListener { e ->
            val notches = e!!.wheelRotation
            val temp = (zoom - notches * 0.1).coerceAtLeast(0.5) // Limit min zoom
            if (temp != zoom) {
                zoom = temp
                resizeImage()
            }
        }

        renderButton.addActionListener { generateUMLImage() }
        textArea.addKeyListener(textAreaHandler())
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun textAreaHandler(): KeyListener =
        object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {
            }

            override fun keyPressed(e: KeyEvent?) {
            }

            override fun keyReleased(e: KeyEvent?) {
                if (e!!.keyCode == KeyEvent.VK_ENTER) {
                    generateUMLImage()
                } else if (e.keyCode == KeyEvent.VK_BACK_SPACE) {
                    val deferred = GraphImpl.isValidFormat(textArea.text.split("\n"))
                    deferred.invokeOnCompletion {
                        if (deferred.getCompleted()) {
                            generateUMLImage()
                        } else if (textArea.text.isEmpty()) {
                            imageLabel.icon = null
                            vertexButtonsPanel.removeAll()
                            vertexButtons.clear()
                        }
                    }
                }
            }
        }

    // 🎨 FUNCTION TO GENERATE UML IMAGE FROM TEXT INPUT
    private fun generateUMLImage() {
        val graphCode = textArea.text.trim()
        println(graphCode)
        if (graphCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Graph code!", "Error", JOptionPane.ERROR_MESSAGE)
            return
        }
        graph = GraphImpl.create(graphCode.split("\n"))

        // Clear existing buttons
        vertexButtonsPanel.removeAll()
        vertexButtons.clear()

        // Add new buttons for each vertex
        graph.vertexs().forEach { vertex ->
            addVertexButton(vertex.getId())
        }

        // Generate the PlantUML image
        generatePlantUMLImage(GraphImpl.toPlantUml(graph))

        // Refresh the UI
        vertexButtonsPanel.revalidate()
        vertexButtonsPanel.repaint()
        pack() // Resize window to fit image
    }

    // 🖼️ FUNCTION TO GENERATE UML IMAGE IN MEMORY
    private fun generatePlantUMLImage(umlString: String) {
        val reader = SourceStringReader(umlString)
        val outputStream = ByteArrayOutputStream()
        reader.outputImage(outputStream).description // Generates image in memory
        imageLabel.icon = ImageIcon(ImageIO.read(outputStream.toByteArray().inputStream())!!)
        originalImage = (imageLabel.icon as ImageIcon).image
    }

    // 🔄 FUNCTION TO ADD A VERTEX BUTTON
    private fun addVertexButton(vertexId: String) {
        val button = JButton("$vertexId (Enabled)")
        button.preferredSize = Dimension(150, 40)
        button.maximumSize = Dimension(Short.MAX_VALUE.toInt(), 40)
        button.alignmentX = Component.CENTER_ALIGNMENT

        button.addActionListener {
            val vertex = VertexImpl(vertexId)
            if (graph.isVertexEnabled(vertex)) {
                button.text = "$vertexId (Disabled)"
                graph.disableVertex(vertex)
            } else {
                button.text = "$vertexId (Enabled)"
                graph.enableVertex(vertex)
            }
            generatePlantUMLImage(GraphImpl.toPlantUml(graph))
        }
        vertexButtons[vertexId] = button

        vertexButtonsPanel.add(button)
        vertexButtonsPanel.add(Box.createRigidArea(Dimension(0, 5))) // Add spacing between buttons
    }

    private fun resizeImage() {
        originalImage?.let {
            val newWidth = (it.getWidth(null) * zoom).toInt()
            val newHeight = (it.getHeight(null) * zoom).toInt()
            val resizedImage = it.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
            imageLabel.icon = ImageIcon(resizedImage)
            imageLabel.revalidate()
            imageLabel.repaint()
        }
    }
}

fun main() {
    SwingUtilities.invokeLater { GUIApp() }
}
