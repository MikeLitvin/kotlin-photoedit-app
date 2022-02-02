package com.example.photoedit

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.TextField
import javafx.scene.input.DataFormat
import kotlin.String

abstract class Edit<T>(nodeState: DataFormat, linkState: DataFormat, id: UInt, private val validatorRegex: Regex):
    ValueNode<T>(nodeState, linkState, id, FXMLLoader(Edit::class.java.getResource("editor.fxml"))) {
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
    override fun load(_x: Double, _y: Double, _value: T?) {
        super.load(_x, _y, _value)
        editField.textProperty().set(_value.toString())
    }

    override fun initOutput() { output = toOutput }

    abstract fun toValue(text: String): T
}