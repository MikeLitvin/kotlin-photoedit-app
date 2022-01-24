package com.example.photoedit

import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.geometry.Point2D
import javafx.scene.input.*
import javafx.scene.layout.AnchorPane


abstract class Node<T>(private val nodeState: DataFormat, private val linkState: DataFormat, loader: FXMLLoader):
    AnchorPane() {

    private val dragOverHandler = EventHandler<DragEvent> { event ->
        moveTo(Point2D(event.sceneX, event.sceneY))
        event.consume()
    }

    private val dragDroppedHandler = EventHandler<DragEvent> { event ->
        parent.onDragOver = null
        parent.onDragDropped = null
        event.isDropCompleted = true
        event.consume()
    }

    val dragDetectedHandler get() = EventHandler<MouseEvent> { event ->
        parent.onDragOver = dragOverHandler
        parent.onDragDropped = dragDroppedHandler
        offset = Point2D(event.x, event.y)
        moveTo(Point2D(event.sceneX, event.sceneY))
        val content = ClipboardContent()
        content[nodeState] = 1
        startDragAndDrop(*TransferMode.ANY).setContent(content)
        event.consume()
    }

    private val contextLinkDragOverHandler = EventHandler<DragEvent> { event ->
        event.acceptTransferModes(*TransferMode.ANY)
        if(!link.isVisible) link.isVisible = true
        link.setEnd(Point2D(event.x, event.y))
        event.consume()
    }

    private val contextLinkDragDroppedHandler = EventHandler<DragEvent> { event ->
        parent.onDragDropped = null
        parent.onDragOver = null
        link.isVisible = false
        superParent!!.children.removeAt(0)
        event.isDropCompleted = true
        event.consume()
    }

    val linkDragDetectedHandler = EventHandler<MouseEvent> { event ->
        if (!link.isConnected) {
            parent.onDragOver = contextLinkDragOverHandler
            parent.onDragDropped = contextLinkDragDroppedHandler
            link.isVisible = true
            link.bindStart(event.source as OutLink<*>)
            superParent!!.children.add(0, link)
            val content = ClipboardContent()
            content[linkState] = "link"
            startDragAndDrop(*TransferMode.ANY).setContent(content)
            event.consume()
        }

    }

    val linkDragDroppedHandler = EventHandler<DragEvent> { event ->
        parent.onDragOver = null
        parent.onDragDropped = null
        val linkDestination = event.source as InputLink<T>
        val linkSource = event.gestureSource as Node<T>
        val connectedLink = (event.gestureSource as Node<T>).link
        if(connectedLink.valueProperty::class == linkDestination.valueProperty::class && !linkDestination.isConnected
            && linkSource != this) {
            connectedLink.bindEnd(linkDestination)
            connectedLink.isConnected = true
            connectedLink.destination = linkDestination
            linkDestination.valueProperty.set(connectedLink.valueProperty.value)
            linkDestination.connectedLink = connectedLink
            connectedLinks.add(connectedLink)
            val content = ClipboardContent()
            content[linkState] = "link"
            startDragAndDrop(*TransferMode.ANY).setContent(content)
        } else {
            parent.onDragDropped = null
            parent.onDragOver = null
            connectedLink.isVisible = false
            linkSource.superParent!!.children.removeAt(0)
            event.isDropCompleted = true
        }
        event.consume()
    }

    private var offset = Point2D(0.0, 0.0)
    private var superParent: AnchorPane? = null
    var link = Linker(this)
    var value: T? = null
    protected val connectedLinks = mutableListOf<Linker<T>>()

    private fun moveTo(point: Point2D) {
        val local = parent.sceneToLocal(point)
        relocate((local.x - offset.x), (local.y - offset.y))
    }

    fun removeLink(link: Linker<T>) {
        superParent!!.children.remove(link)
        link.isConnected = false
        link.unbindEnd()
        link.destination?.connectedLink = null
        link.destination?.valueProperty?.set(link.destination?.defaultValue)
        link.destination = null
    }
    init {
        loader.setController(this)
        children.add(loader.load())
        parentProperty().addListener { _, _, _ -> parent?.let { superParent = parent as AnchorPane } }
        link.setOnMouseClicked { removeLink(link) }
    }
}