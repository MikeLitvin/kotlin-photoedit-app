package com.example.photoedit

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.DataFormat
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox

abstract class ValueNode<T>(nodeState: DataFormat, linkState: DataFormat, id: UInt, loader: FXMLLoader):
    Node<T>(nodeState, linkState, id, loader) {
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

    private fun removeAllLinks() {
        val allLinks = connectedLinks.plus(link)
        val linksIterator = allLinks.iterator()

        for(link in linksIterator) {
            removeLink(link)
        }
    }

    @FXML
    open fun initialize() {
        draggedArea.onDragDetected = dragDetectedHandler
        deleteButton.setOnAction {
            (parent as AnchorPane).children.remove(this)
            removeAllLinks()
            onNodeRemovedCallback(this)
        }
    }
}