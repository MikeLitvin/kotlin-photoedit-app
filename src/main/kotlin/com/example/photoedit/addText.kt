package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.Color
import java.awt.Font

class addText(node: DataFormat, link: DataFormat): filter(node, link) {
    private lateinit var x: inputLink<Int?>
    private lateinit var y: inputLink<Int?>
    private lateinit var fontSize: inputLink<Int?>
    private lateinit var text: inputLink<String?>

    @FXML
    override fun setTitle() { nodeName.text = "Add Text" }

    override fun initialize() {
        super.initialize()
        x = inputLink(null)
        y = inputLink(null)
        text = inputLink(null)
        fontSize = inputLink(null)
        inputs = mapOf(Pair(x, "x"), Pair(y, "y"), Pair(text, "Text"), Pair(fontSize, "Size"))
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        val font = Font("Arial", Font.BOLD, fontSize.valueProperty.value!!)
        val graphics = bufferedImage.graphics
        graphics.font = font
        graphics.color = Color.BLACK
        graphics.drawString(text.valueProperty.value!!, x.valueProperty.value!!, y.valueProperty.value!!)
        return SwingFXUtils.toFXImage(bufferedImage, null)
    }
}