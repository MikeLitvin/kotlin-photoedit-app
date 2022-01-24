package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class Gray(nodeState: DataFormat, linkState: DataFormat): Filter(nodeState, linkState) {

    @FXML
    override fun setTitle() { nodeName.text = "Gray Filter" }

    override fun initialize() {
        super.initialize()
        inputs = mapOf()
    }

    override fun filterFunction(img: Image): Image {
        val tmpMat = imageToMat(img)
        val resultMat = Mat()
        Imgproc.cvtColor(tmpMat, resultMat, Imgproc.COLOR_RGB2GRAY)
        return matToImage(resultMat)
    }
}