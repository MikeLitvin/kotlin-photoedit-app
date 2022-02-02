package com.example.photoedit

import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.geometry.Point2D
import javafx.scene.input.*
import javafx.scene.layout.AnchorPane
import java.io.Serializable

const val FloatNodeType = "Float"
const val IntNodeType = "Int"
const val StringNodeType = "String"
const val ImageNodeType = "Image"
const val StartNodeType = "Start"
const val EndNodeType = "End"
const val AddImageNodeType = "AddImage"
const val AddTextNodeType = "AddText"
const val BlurNodeType = "Blur"
const val  BrightnessNodeType = "Brightness"
const val GrayFilterNodeType = "Gray"
const val InvertNodeType = "Invert"
const val MoveNodeType = "Move"
const val RotationNodeType = "Rotation"
const val ScaleNodeType = "Scale"
const val SepiaNodeType = "Sepia"


abstract class Node<T>(private val nodeState: DataFormat, private val linkState: DataFormat, val id: UInt, loader: FXMLLoader):
    AnchorPane(), Serializable {

    var type: String private set

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
            connectLink(connectedLink, linkDestination)
//            connectedLink.bindEnd(linkDestination)
//            connectedLink.isConnected = true
//            connectedLink.destination = linkDestination
//            linkDestination.valueProperty.set(connectedLink.valueProperty.value)
//            linkDestination.connectedLink = connectedLink
//            connectedLinks.add(connectedLink)
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

    val connectedLinks = mutableListOf<Linker<*>>()
    var linkInputs = mutableListOf<InputLink<*>>()
    var valueProperty = SimpleObjectProperty<T?>()

    var output: OutLink<T>? = null

    private fun moveTo(point: Point2D) {
        val local = parent.sceneToLocal(point)
        relocate((local.x - offset.x), (local.y - offset.y))
    }

    fun removeLink(link: Linker<*>) {
        superParent!!.children.remove(link)
        link.isConnected = false
        link.unbindEnd()
//        link.destination?.connectedLink = null
//        link.destination?.valueProperty?.set(link.destination?.defaultValue)
//        link.destination = null
    }

    fun <E> connectLink(
        connectedLink: Linker<E>,
        linkDestination: InputLink<E>,
    ) {
        connectedLink.bindEnd(linkDestination)
        connectedLink.isConnected = true
        connectedLink.destination = linkDestination
        linkDestination.valueProperty.set(connectedLink.valueProperty.value)
        linkDestination.connectedLink = connectedLink
        connectedLinks.add(connectedLink)

        val content = ClipboardContent()

        content[linkState] = "link"
        startDragAndDrop(*TransferMode.ANY).setContent(content)
    }

    fun <E> loadLink(
        connectedLink: Linker<E>,
        linkDestination: InputLink<E>
    ) {
        connectLink(connectedLink, linkDestination)
        superParent!!.children.add(0, connectedLink)
        connectedLink.source.output?.let { connectedLink.bindStart(it) }
    }

    var onNodeRemovedCallback: (Node<T>) -> Unit = {}

    open fun load(_x: Double, _y: Double, _value: T?) {
        layoutX = _x
        layoutY = _y
        value = _value

        valueProperty.set(_value)
    }

    abstract fun initOutput()

    abstract fun initType(): String

    abstract fun initInputs()

    init {
        loader.setController(this)
        children.add(loader.load())
        parentProperty().addListener { _, _, _ -> parent?.let { superParent = parent as AnchorPane } }
        link.setOnMouseClicked { removeLink(link) }
        type = initType()
    }
}