package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.image.WritablePixelFormat
import javafx.scene.input.DataFormat
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayInputStream

class Blur(nodeState: DataFormat, linkState: DataFormat): Filter(nodeState, linkState) {
    private lateinit var kernelSizeValue: InputLink<Int?>

    @FXML
    override fun setTitle() { nodeName.text = "Blur" }

    override fun initialize() {
        super.initialize()
        kernelSizeValue = InputLink(null)
        inputs = mapOf(Pair(kernelSizeValue, "Kernel size"))
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val src = imageToMat(img)
        val res = Mat()
        var kernelSize = kernelSizeValue.valueProperty.value!!
        if (kernelSize % 2 == 0) kernelSize++
        Imgproc.GaussianBlur(src, res, Size(kernelSize.toDouble(), kernelSize.toDouble()), 0.0)
        return matToImage(res)
    }
}


fun imageToMat(image: Image): Mat {
    val width: Int = image.width.toInt()
    val height: Int = image.height.toInt()
    val buffer = ByteArray(width * height * 4)
    val pixelReader = image.pixelReader
    val format = WritablePixelFormat.getByteBgraInstance()
    pixelReader.getPixels(0, 0, width, height, format, buffer, 0, width * 4)
    val mat = Mat(height, width, CvType.CV_8UC4)
    mat.put(0, 0, buffer)
    return mat
}

fun matToImage(mat: Mat): Image {
    val buffer = MatOfByte()
    Imgcodecs.imencode(".png", mat, buffer)
    return Image(ByteArrayInputStream(buffer.toArray()))
}