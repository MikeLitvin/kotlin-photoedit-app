package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.input.DataFormat
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import nu.pattern.OpenCV
import java.awt.image.BufferedImage
import java.io.File

class PhotoeditController {

    @FXML
    private lateinit var sceneContainer: AnchorPane

    @FXML
    private lateinit var floatNodeButton: Button

    @FXML
    private lateinit var intNodeButton: Button

    @FXML
    private lateinit var stringNodeButton: Button

    @FXML
    private lateinit var imageNodeButton: Button

    @FXML
    private lateinit var addTextNodeButton: Button

    @FXML
    private lateinit var addImageNodeButton: Button

    @FXML
    private lateinit var grayNodeButton: Button

    @FXML
    private lateinit var brightnessNodeButton: Button

    @FXML
    private lateinit var sepiaNodeButton: Button

    @FXML
    private lateinit var invertNodeButton: Button

    @FXML
    private lateinit var blurNodeButton: Button

    @FXML
    private lateinit var moveNodeButton: Button

    @FXML
    private lateinit var scaleNodeButton: Button

    @FXML
    private lateinit var rotateNodeButton: Button

    @FXML
    private lateinit var saveSceneButton: Button;

    @FXML
    private lateinit var loadSceneButton: Button;

    private fun <T> addNode(node: Node<T>) {
        node.onNodeRemovedCallback = {
        scene.remove(it)
    }
        sceneContainer.children.add(node)
        scene.add(node)
    }

    private val node = DataFormat("node")
    private val link = DataFormat("link")

    private var scene = Scene(node, link, 0u)

    fun initialize(){
        OpenCV.loadLocally()

        // Можно добавлять к нодам эффектов все необходимые ноды (?)


        floatNodeButton.setOnAction { addNode(FloatValue(node, link, scene.getId())) }
        intNodeButton.setOnAction{ addNode(IntValue(node, link, scene.getId())) }
        stringNodeButton.setOnAction{ addNode(StringValue(node, link, scene.getId())) }
        imageNodeButton.setOnAction{ addNode(ImageNode(node, link, scene.getId())) }
        addTextNodeButton.setOnAction{ addNode(AddText(node, link, scene.getId())) }
        addImageNodeButton.setOnAction{ addNode(AddImage(node, link, scene.getId())) }
        grayNodeButton.setOnAction{ addNode(Gray(node, link, scene.getId())) }
        brightnessNodeButton.setOnAction{ addNode(Brightness(node, link, scene.getId())) }
        sepiaNodeButton.setOnAction{ addNode(Sepia(node, link, scene.getId())) }
        invertNodeButton.setOnAction{ addNode(Invert(node, link, scene.getId())) }
        blurNodeButton.setOnAction{ addNode(Blur(node, link, scene.getId())) }
        moveNodeButton.setOnAction{ addNode(Move(node, link, scene.getId())) }
        scaleNodeButton.setOnAction{ addNode(Scale(node, link, scene.getId())) }
        rotateNodeButton.setOnAction{ addNode(Rotation(node, link, scene.getId())) }
        addNode(StartNode(node, link, scene.getId()))
        addNode(EndNode(node, link, scene.getId()).also { it.layoutX = 700.0 })


        saveSceneButton.setOnAction {
            val json = scene.save()
            val fileChooser = FileChooser()
            val extensionFilter = FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
            fileChooser.extensionFilters.add(extensionFilter)
            var file = fileChooser.showSaveDialog(sceneContainer.scene.window as Stage)
            if (file.nameWithoutExtension == file.name) { file = File(file.parentFile, file.nameWithoutExtension + ".json") }
            file.writeText(json)
        }

        loadSceneButton.setOnAction {
            val fileChooser = FileChooser()
            val extensionFilter = FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
            fileChooser.extensionFilters.add(extensionFilter)
            val file = fileChooser.showOpenDialog(sceneContainer.scene.window as Stage)
            val json = file.readText()
            scene = scene.load(json)
            clearScene()
            val nodesIterator = scene.nodes.iterator()
            for(node in nodesIterator) { sceneContainer.children.add(node) }
            loadLinks()
        }
    }
    private fun clearScene() { sceneContainer.children.clear() }

    private fun loadLinks() { for (nodeConnections in scene.connections) {
            val node = scene.findNodeById(nodeConnections.id.toUInt())
            node?.let { for (connectionKey in nodeConnections.connections) {
                    val connectedNode = scene.findNodeById(connectionKey.nodeId.toUInt())
                    connectedNode.let {
                        val connectedLink = connectedNode!!.link
                        val currentInput = node.linkInputs[connectionKey.inputId]
                        when {
                            connectedLink.valueProperty.value is Int? && currentInput.valueProperty.value is Int? ->
                                node.loadLink(connectedLink as Linker<Int?>, currentInput as InputLink<Int?>)

                            connectedLink.valueProperty.value is Float? && currentInput.valueProperty.value is Float? ->
                                node.loadLink(connectedLink as Linker<Float?>, currentInput as InputLink<Float?>)

                            connectedLink.valueProperty.value is String? && currentInput.valueProperty.value is String? ->
                                node.loadLink(connectedLink as Linker<String?>, currentInput as InputLink<String?>)

                            connectedLink.valueProperty.value is BufferedImage? && currentInput.valueProperty.value is BufferedImage? ->
                                node.loadLink(connectedLink as Linker<BufferedImage?>, currentInput as InputLink<BufferedImage?>)
                        }
                    }
                }
            }
        }
    }
}