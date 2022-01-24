package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat

class addImage(node: DataFormat, link: DataFormat): filter(node, link) {
    private lateinit var x: inputLink<Int?>
    private lateinit var y: inputLink<Int?>
    private lateinit var newImage: inputLink<Image?>

    @FXML
    override fun setTitle() { nodeName.text = "Add Image" }

    override fun initialize() {
        super.initialize()
        x = inputLink(null)
        y = inputLink(null)
        newImage = inputLink(null)
        inputs = mapOf(Pair(newImage, "Image"), Pair(x, "x"), Pair(y, "y"))
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        val graphics = bufferedImage.graphics
        graphics.drawImage(SwingFXUtils.fromFXImage(newImage.valueProperty.value!!, null),
            x.valueProperty.value!!, y.valueProperty.value!!, null)
        return SwingFXUtils.toFXImage(bufferedImage, null)
    }
}