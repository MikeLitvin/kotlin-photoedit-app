package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.input.DataFormat
import javafx.scene.layout.AnchorPane
import nu.pattern.OpenCV

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

    private fun <T> addNode(node: Node<T>) { sceneContainer.children.add(node) }

    private val node = DataFormat("node")
    private val link = DataFormat("link")

    fun initialize(){
        OpenCV.loadLocally()

        // Можно добавлять к нодам эффектов все необходимые ноды (?)

        addNode(StartNode(node, link).also {
            it.layoutY = 150.0
        })
        addNode(EndNode(node, link).also {
            it.layoutX = 980.0
        }.also {
            it.layoutY = 150.0
        })

        floatNodeButton.setOnAction {
            addNode(FloatValue(node, link))
        }
        intNodeButton.setOnAction{
            addNode(IntValue(node, link))
        }
        stringNodeButton.setOnAction{
            addNode(StringValue(node, link))
        }
        imageNodeButton.setOnAction{
            addNode(ImageNode(node, link))
        }
        addTextNodeButton.setOnAction{
            addNode(AddText(node, link))
        }
        addImageNodeButton.setOnAction{
            addNode(AddImage(node, link))
        }
        grayNodeButton.setOnAction{
            addNode(Gray(node, link))
        }
        brightnessNodeButton.setOnAction{
            addNode(Brightness(node, link))
        }
        sepiaNodeButton.setOnAction{
            addNode(Sepia(node, link))
        }
        invertNodeButton.setOnAction{
            addNode(Invert(node, link))
        }
        blurNodeButton.setOnAction{
            addNode(Blur(node, link))
        }
        moveNodeButton.setOnAction{
            addNode(Move(node, link))
        }
        scaleNodeButton.setOnAction{
            addNode(Scale(node, link))
        }
        rotateNodeButton.setOnAction{
            addNode(Rotation(node, link))
        }
    }
}