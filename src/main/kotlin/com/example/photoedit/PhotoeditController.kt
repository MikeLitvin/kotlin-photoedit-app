package com.example.photoedit

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox

class PhotoeditController {
    @FXML
    private lateinit var sceneSrollBox: ScrollPane

    @FXML
    private lateinit var sceneContainer: AnchorPane

    @FXML
    private lateinit var menuScroll: ScrollPane

    @FXML
    private lateinit var menuContainer: VBox

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

    fun addNode(){

    }

    fun initialize(){
        floatNodeButton.setOnAction {
            addNode()
        }
        intNodeButton.setOnAction{
            addNode()
        }
        stringNodeButton.setOnAction{
            addNode()
        }
        imageNodeButton.setOnAction{
            addNode()
        }
        addTextNodeButton.setOnAction{
            addNode()
        }
        addImageNodeButton.setOnAction{
            addNode()
        }
        grayNodeButton.setOnAction{
            addNode()
        }
        brightnessNodeButton.setOnAction{
            addNode()
        }
        sepiaNodeButton.setOnAction{
            addNode()
        }
        invertNodeButton.setOnAction{
            addNode()
        }
        blurNodeButton.setOnAction{
            addNode()
        }
        moveNodeButton.setOnAction{
            addNode()
        }
        scaleNodeButton.setOnAction{
            addNode()
        }
        rotateNodeButton.setOnAction{
            addNode()
        }
    }
}