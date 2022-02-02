package com.example.photoedit

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import javafx.scene.layout.RowConstraints
import javafx.scene.text.Font
import java.awt.image.BufferedImage

abstract class Filter(node: DataFormat, link: DataFormat, id: UInt): BaseImageNode(node, link, id) {
    lateinit var inputImage: InputLink<BufferedImage>
    lateinit var outputImage: OutLink<BufferedImage>
    lateinit var inputs: Map<InputLink<*>, String>

    @FXML
    override fun initialize() {
        super.initialize()
        setTitle()
        inputImage = InputLink(null, this)
        inputImage.valueProperty.addListener { _, _, newValue ->
            newValue?.let {
                val filteredImage = filterImage(SwingFXUtils.toFXImage(newValue, null))
                filteredImage?.let {
                    valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                    link.valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                    image.image = filteredImage
                }
            }
        }

        inputImage.onDragDropped = linkDragDroppedHandler
        grid.add(inputImage, 0, 2)

        outputImage = OutLink()
        outputImage.onDragDetected = linkDragDetectedHandler
        grid.add(outputImage, 2, 2)
    }

    protected fun bindInputs() {
        for(input in inputs) {
            input.key.onDragDropped = linkDragDroppedHandler
            input.key.valueProperty.addListener {
                    _, _, _ ->
                val filteredImage = filterImage(
                    SwingFXUtils.toFXImage(inputImage.valueProperty.value, null)
                )
                filteredImage.let {
                    valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                    link.valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                }
                image.image = filteredImage
            }
        }
    }

    protected fun addInputs(startRow: Int) {
        var currentRow = startRow
        inputs.forEach { entry ->
            grid.rowConstraints.add(RowConstraints(60.0))
            grid.add(entry.key, 0, currentRow)
            grid.add(Label(entry.value).also { it.font = Font(14.0) }, 1, currentRow)
            currentRow += 1
        }
    }
    open fun filterImage(img: Image?): Image? {
        for (input in inputs) if (input.key.valueProperty.value == null) return null
        if (img == null) return null
        return filterFunction(img)
    }

    override fun initOutput() {
        output = outputImage
    }

    abstract fun filterFunction(img: Image): Image
    abstract fun setTitle()
}