package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage

class AddText(node: DataFormat, link: DataFormat, id: UInt): Filter(node, link, id) {
    private lateinit var x: InputLink<Int?>
    private lateinit var y: InputLink<Int?>
    private lateinit var fontSize: InputLink<Int?>
    private lateinit var text: InputLink<String?>

    @FXML
    override fun setTitle() { nodeName.text = "Add Text" }

    override fun initialize() {
        super.initialize()
        x = InputLink(null, this)
        y = InputLink(null, this)
        text = InputLink(null, this)
        fontSize = InputLink(null, this)
        inputs = mapOf(Pair(x, "x"), Pair(y, "y"), Pair(text, "Text"), Pair(fontSize, "Size"))
        initInputs()
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

    override fun initType(): String = AddTextNodeType

    override fun initInputs() { linkInputs.addAll(listOf(inputImage, x, y, text)) }
}