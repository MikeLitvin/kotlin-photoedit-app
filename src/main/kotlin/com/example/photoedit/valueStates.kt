package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.input.DataFormat

class string(nodeState: DataFormat, linkState: DataFormat):
    edit<String>(nodeState, linkState, Regex("^[\\s\\S]*")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set("")
        nodeName.text = "String"
    }
    override fun toValue(text: String): String = text
}

class float(nodeState: DataFormat, linkState: DataFormat):
    edit<Float>(nodeState, linkState, Regex("[+-]?([0-9]*[.])?[0-9]+")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set(0.0f)
        nodeName.text = "Float"
    }
    override fun toValue(text: String): Float = text.toFloat()
}

class int(nodeState: DataFormat, linkState: DataFormat):
    edit<Int>(nodeState, linkState, Regex("^[+-]?\\d+\$")) {
    @FXML
    override fun initialize() {
        super.initialize()
        link.valueProperty.set(0)
        nodeName.text = "Int"
    }
    override fun toValue(text: String): Int = text.toInt()
}