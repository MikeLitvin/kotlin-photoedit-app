package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.input.DataFormat

class StringValue(nodeState: DataFormat, linkState: DataFormat):
    Edit<String>(nodeState, linkState, Regex("^[\\s\\S]*")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set("")
        nodeName.text = "String"
    }
    override fun toValue(text: kotlin.String): String = text
}

class FloatValue(nodeState: DataFormat, linkState: DataFormat):
    Edit<Float>(nodeState, linkState, Regex("[+-]?([0-9]*[.])?[0-9]+")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set(0.0f)
        nodeName.text = "Float"
    }
    override fun toValue(text: kotlin.String): Float = text.toFloat()
}

class IntValue(nodeState: DataFormat, linkState: DataFormat):
    Edit<Int>(nodeState, linkState, Regex("^[+-]?\\d+\$")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set(0)
        nodeName.text = "Int"
    }
    override fun toValue(text: String): Int = text.toInt()
}