package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat

class Brightness(nodeState: DataFormat, linkState: DataFormat, id: UInt) : Filter(nodeState, linkState, id) {
    private lateinit var brightnessLevel: InputLink<Float?>

    @FXML
    override fun setTitle() { nodeName.text = "Brightness" }
    override fun initialize() {
        super.initialize()
        brightnessLevel = InputLink(null, this)
        inputs = mapOf(Pair(brightnessLevel, "Level"))
        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val resultMat = imageToMat(img)
        imageToMat(img).convertTo(resultMat, -1, 1.0,
            brightnessLevel.valueProperty.value!!.toDouble() * 100)
        return matToImage(resultMat)
    }

    override fun initType(): String = BrightnessNodeType

    override fun initInputs() { linkInputs.addAll(listOf(inputImage, brightnessLevel)) }
}