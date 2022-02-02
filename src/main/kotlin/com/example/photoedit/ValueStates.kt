package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.input.DataFormat

open class StringValue(nodeState: DataFormat, linkState: DataFormat, id: UInt):
    Edit<String>(nodeState, linkState, id, Regex("^[\\s\\S]*")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set("")
        nodeName.text = "String"
    }
    override fun toValue(text: String): String = text
    override fun initType(): String = StringNodeType
    override fun initInputs() {
    }
}

open class FloatValue(nodeState: DataFormat, linkState: DataFormat, id: UInt):
    Edit<Float>(nodeState, linkState, id, Regex("[+-]?([0-9]*[.])?[0-9]+")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set(0.0f)
        nodeName.text = "Float"
    }
    override fun toValue(text: String): Float = text.toFloat()
    override fun initType() = FloatNodeType
    override fun initInputs() {
    }
}

open class IntValue(nodeState: DataFormat, linkState: DataFormat, id: UInt):
    Edit<Int>(nodeState, linkState, id, Regex("^[+-]?\\d+\$")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set(0)
        nodeName.text = "Int"
    }
    override fun toValue(text: String): Int = text.toInt()
    override fun initType(): String = IntNodeType
    override fun initInputs() {
    }
}