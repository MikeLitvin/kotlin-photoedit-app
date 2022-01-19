package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import javafx.scene.layout.RowConstraints
import javafx.stage.FileChooser
import javafx.stage.Stage
import javax.imageio.ImageIO

open class imageNode(nodeState: DataFormat, linkState: DataFormat): baseImageNode(nodeState, linkState) {
    @FXML
    override fun initialize() {
        super.initialize()

        nodeName.text = "Image"
        grid.rowConstraints.add(RowConstraints(100.0))
        val openButton = Button("Open image")
        grid.add(openButton, 1, 3)
        openButton.setOnAction {
            val img = importImage()
            valueProperty.set(img)
            image.image = valueProperty.value
        }
        val output = outLink<Image>()
        output.onDragDetected = linkDragDetectedHandler
        grid.add(output, 2, 2)
    }

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