package com.example.photoedit

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class PhotoeditApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(PhotoeditApplication::class.java.getResource("main.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "Photo Editor"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(PhotoeditApplication::class.java)
}