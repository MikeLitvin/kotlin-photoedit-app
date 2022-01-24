package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import javafx.scene.layout.RowConstraints
import javafx.scene.text.Font

abstract class filter(node: DataFormat, link: DataFormat): baseImageNode(node, link) {
    lateinit var inputImage: inputLink<Image>
    lateinit var outputImage: outLink<Image>
    lateinit var inputs: Map<inputLink<*>, String>

    @FXML
    override fun initialize() {
        super.initialize()
        setTitle()
        inputImage = inputLink(null)
        inputImage.valueProperty.addListener { _, _, newValue ->
            val filteredImage = filterImage(newValue)
            valueProperty.value = filteredImage
            link.valueProperty.value = filteredImage
            image.image = filteredImage
        }

        inputImage.onDragDropped = linkDragDroppedHandler
        grid.add(inputImage, 0, 2)

        outputImage = outLink()
        outputImage.onDragDetected = linkDragDetectedHandler
        grid.add(outputImage, 2, 2)
    }

    protected fun bindInputs() {
        for(input in inputs) {
            input.key.onDragDropped = linkDragDroppedHandler
            input.key.valueProperty.addListener { _, _, _ ->
                val filteredImage = filterImage(inputImage.valueProperty.value)
                valueProperty.value = filteredImage
                link.valueProperty.value = filteredImage
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

    abstract fun filterFunction(img: Image): Image
    abstract fun setTitle()
}