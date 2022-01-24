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

    private fun <T> addNode(node: node<T>) { sceneContainer.children.add(node) }

    private val node = DataFormat("node")
    private val link = DataFormat("link")

    fun initialize(){
        OpenCV.loadLocally()

        // Можно добавлять к нодам эффектов все необходимые ноды (?)

        addNode(startNode(node, link).also {
            it.layoutY = 150.0
        })
        addNode(endNode(node, link).also {
            it.layoutX = 980.0
        }.also {
            it.layoutY = 150.0
        })

        floatNodeButton.setOnAction {
            addNode(float(node, link))
        }
        intNodeButton.setOnAction{
            addNode(int(node, link))
        }
        stringNodeButton.setOnAction{
            addNode(string(node, link))
        }
        imageNodeButton.setOnAction{
            addNode(imageNode(node, link))
        }
        addTextNodeButton.setOnAction{
            addNode(addText(node, link))
        }
        addImageNodeButton.setOnAction{
            addNode(addImage(node, link))
        }
        grayNodeButton.setOnAction{
            addNode(gray(node, link))
        }
        brightnessNodeButton.setOnAction{
            addNode(brightness(node, link))
        }
        sepiaNodeButton.setOnAction{
            addNode(sepia(node, link))
        }
        invertNodeButton.setOnAction{
            addNode(invert(node, link))
        }
        blurNodeButton.setOnAction{
            addNode(blur(node, link))
        }
        moveNodeButton.setOnAction{
            addNode(move(node, link))
        }
        scaleNodeButton.setOnAction{
            addNode(scale(node, link))
        }
        rotateNodeButton.setOnAction{
            addNode(rotation(node, link))
        }
    }
}