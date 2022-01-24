package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import kotlin.math.floor

class Scale(nodeState: DataFormat, linkState: DataFormat): Filter(nodeState, linkState){
    private lateinit var x: InputLink<Float?>
    private lateinit var y: InputLink<Float?>

    @FXML
    override fun setTitle() { nodeName.text = "Scale" }

    override fun initialize() {
        super.initialize()
        x = InputLink(null)
        y = InputLink(null)
        inputs = mapOf(Pair(x, "x"), Pair(y, "y"))
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        val xFactor = x.valueProperty.value!!
        val yFactor = y.valueProperty.value!!
        var scaledImage = BufferedImage(floor(bufferedImage.width * xFactor).toInt(),
            floor(bufferedImage.height * yFactor).toInt(), BufferedImage.TYPE_INT_ARGB)
        val affineTransform = AffineTransform.getScaleInstance(xFactor.toDouble(), yFactor.toDouble())
        val scaleOp = AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BICUBIC)
        scaledImage = scaleOp.filter(bufferedImage, scaledImage)
        return SwingFXUtils.toFXImage(scaledImage, null)
    }
}