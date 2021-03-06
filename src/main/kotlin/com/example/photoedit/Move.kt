package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class Move(nodeState: DataFormat, linkState: DataFormat, id: UInt): Filter(nodeState, linkState, id){
    private lateinit var x: InputLink<Float?>
    private lateinit var y: InputLink<Float?>

    @FXML
    override fun setTitle() { nodeName.text = "Move" }

    override fun initialize() {
        super.initialize()
        x = InputLink(null, this)
        y = InputLink(null, this)
        inputs = mapOf(Pair(x, "x"), Pair(y, "y"))
        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val mat = imageToMat(img)
        val translateMat = Mat(2, 3, CvType.CV_64FC1)
        translateMat.put(0, 0, 1.0, 0.0, x.valueProperty.value!!.toDouble(), 0.0, 1.0,
            y.valueProperty.value!!.toDouble())
        Imgproc.warpAffine(mat, mat, translateMat, mat.size())
        return matToImage(mat)
    }

    override fun initType(): String = MoveNodeType

    override fun initInputs() { linkInputs.addAll(listOf(inputImage, x, y)) }

}