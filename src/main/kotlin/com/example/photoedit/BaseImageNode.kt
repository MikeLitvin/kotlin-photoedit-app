package com.example.photoedit

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.image.ImageView
import javafx.scene.input.DataFormat
import javafx.scene.layout.GridPane
import java.awt.image.BufferedImage

abstract class BaseImageNode(node: DataFormat, link: DataFormat, id: UInt):
    ValueNode<BufferedImage>(node, link, id, FXMLLoader(BaseImageNode::class.java.getResource("image.fxml")))
{
    @FXML
    lateinit var image: ImageView

    @FXML
    lateinit var grid: GridPane

    @FXML
    override fun initialize() {
        super.initialize()

        val imageColumn = grid.columnConstraints[1]
        val imageRow = grid.rowConstraints[2]

        valueProperty = SimpleObjectProperty()
        image.fitWidthProperty().bind(Bindings.add(imageColumn.prefWidthProperty(), -10.0))
        image.fitHeightProperty().bind(Bindings.add(imageRow.prefHeightProperty(), - 10.0))
        valueProperty.addListener { _, _, newValue ->
            link.valueProperty.set(newValue)
            value = newValue
        }
    }
}