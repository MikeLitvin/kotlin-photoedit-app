package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.image.BufferedImage

class AddImage(node: DataFormat, link: DataFormat, id: UInt): Filter(node, link, id) {
    private lateinit var x: InputLink<Int?>
    private lateinit var y: InputLink<Int?>
    private lateinit var newImage: InputLink<BufferedImage?>

    @FXML
    override fun setTitle() { nodeName.text = "Add Image" }

    override fun initialize() {
        super.initialize()
        x = InputLink(null, this)
        y = InputLink(null, this)
        newImage = InputLink(null, this)
        inputs = mapOf(Pair(newImage, "Image"), Pair(x, "x"), Pair(y, "y"))
        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        val graphics = bufferedImage.graphics
        graphics.drawImage(newImage.valueProperty.value!!, x.valueProperty.value!!, y.valueProperty.value!!, null)
        return SwingFXUtils.toFXImage(bufferedImage, null)
    }

    override fun initType(): String = AddImageNodeType

    override fun initInputs() { linkInputs.addAll(listOf(inputImage, x, y, newImage)) }
}