package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.Color

class Invert(nodeState: DataFormat, linkState: DataFormat): Filter(nodeState, linkState) {

    @FXML
    override fun setTitle() { nodeName.text = "Invert" }

    override fun initialize() {
        super.initialize()
        inputs = mapOf()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        for (x in 0 until bufferedImage.width) {
            for (y in 0 until bufferedImage.height) {
                val rgba = bufferedImage.getRGB(x, y)
                val color = Color(rgba, true)
                val invertedColor = Color(255 - color.red, 255 - color.green, 255 - color.blue)
                bufferedImage.setRGB(x, y, invertedColor.rgb)
            }
        }
        return SwingFXUtils.toFXImage(bufferedImage, null)
    }
}