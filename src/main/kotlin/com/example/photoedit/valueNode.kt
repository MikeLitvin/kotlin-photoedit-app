package com.example.photoedit

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.DataFormat
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox

abstract class valueNode<T>(nodeState: DataFormat, linkState: DataFormat, loader: FXMLLoader):
    node<T>(
    nodeState, linkState, loader) {
    @FXML
    lateinit var nodeName: Label

    @FXML
    lateinit var inputLayout: VBox

    @FXML
    lateinit var outputLayout: VBox

    @FXML
    lateinit var draggedArea: AnchorPane

    @FXML
    lateinit var deleteButton: Button

    @FXML
    open fun initialize() {
        draggedArea.onDragDetected = dragDetectedHandler
        deleteButton.setOnAction {
            (parent as AnchorPane).children.remove(this)
            for(link: linker<T> in connectedLinks) removeLink(link)
            removeLink(link)
        }
    }
}