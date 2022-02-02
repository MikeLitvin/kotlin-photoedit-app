package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import javafx.scene.layout.RowConstraints
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

open class ImageNode(nodeState: DataFormat, linkState: DataFormat, id: UInt): BaseImageNode(nodeState, linkState, id) {
    lateinit var imageOutput: OutLink<BufferedImage>
    @FXML
    override fun initialize() {
        super.initialize()

        nodeName.text = "Image"
        grid.rowConstraints.add(RowConstraints(100.0))
        val openButton = Button("Open image")
        grid.add(openButton, 1, 3)
        openButton.setOnAction {
            val img = importImage()
            valueProperty.set(SwingFXUtils.fromFXImage(img, null))
            image.image = img
        }
        imageOutput = OutLink()
        imageOutput.onDragDetected = linkDragDetectedHandler
        initOutput()
        grid.add(imageOutput, 2, 2)

        valueProperty.addListener {
                _, _, newValue ->
            newValue.let {
                image.image = SwingFXUtils.toFXImage(newValue, null)
            }
        }
    }

    override fun initType(): String = ImageNodeType
    override fun initInputs() {}

    override fun initOutput() { output = imageOutput }

    private fun importImage(): Image {
        val fileChooser = FileChooser()
        val extensionFilters = listOf(
            FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"),
            FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg"),
            FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg")
        )
        fileChooser.extensionFilters.addAll(extensionFilters)
        val file = fileChooser.showOpenDialog(scene.window as Stage)
        return SwingFXUtils.toFXImage(ImageIO.read(file.inputStream()), null)
    }
}