package com.example.photoedit

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.TextField
import javafx.scene.input.DataFormat
import kotlin.String

abstract class Edit<T>(nodeState: DataFormat, linkState: DataFormat, private val validatorRegex: Regex):
    ValueNode<T>(nodeState, linkState, FXMLLoader(Edit::class.java.getResource("editor.fxml"))) {
    protected lateinit var toOutput: OutLink<T>

    @FXML
    protected lateinit var editField: TextField

    @FXML
    override fun initialize() {
        super.initialize()
        toOutput = OutLink()
        toOutput.onDragDetected = linkDragDetectedHandler
        outputLayout.children.add(toOutput)
        draggedArea.onDragDetected = dragDetectedHandler
        editField.textProperty().addListener { _, _, new ->
            if (!new.matches(validatorRegex)) {
                toOutput.onDragDetected = null
            }
            else {
                toOutput.onDragDetected = linkDragDetectedHandler
                value = toValue(editField.text)
                link.valueProperty.set(value)
            }
        }
    }
    abstract fun toValue(text: String): T
}