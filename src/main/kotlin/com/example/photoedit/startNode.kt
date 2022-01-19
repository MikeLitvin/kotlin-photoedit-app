package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.input.DataFormat

class startNode(nodeState: DataFormat, linkState: DataFormat): imageNode(nodeState, linkState) {
    @FXML
    override fun initialize() {
        super.initialize()
        nodeName.text = "Start Node"
        grid.children.remove(deleteButton)
    }
}