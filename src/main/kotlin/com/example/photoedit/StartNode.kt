package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.input.DataFormat

class StartNode(nodeState: DataFormat, linkState: DataFormat, id: UInt): ImageNode(nodeState, linkState, id) {
    @FXML
    override fun initialize() {
        super.initialize()
        nodeName.text = "Start Node"
        grid.children.remove(deleteButton)
    }

    override fun initType(): String = StartNodeType
}