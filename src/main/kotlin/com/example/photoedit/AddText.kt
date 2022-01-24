package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.Color
import java.awt.Font

class AddText(node: DataFormat, link: DataFormat): Filter(node, link) {
    private lateinit var x: InputLink<Int?>
    private lateinit var y: InputLink<Int?>
    private lateinit var fontSize: InputLink<Int?>
    private lateinit var text: InputLink<String?>

    @FXML
    override fun setTitle() { nodeName.text = "Add Text" }

    override fun initialize() {
        super.initialize()
        x = InputLink(null)
        y = InputLink(null)
        text = InputLink(null)
        fontSize = InputLink(null)
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