package com.example.photoedit

import com.example.photoedit.Node
import javafx.scene.input.DataFormat
import com.google.gson.*
import javafx.geometry.Bounds
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Serializable
import javax.imageio.ImageIO
import java.lang.reflect.Type

 class Scene(val nodeState: DataFormat, val linkState: DataFormat, private var currentId: UInt) {
        val nodes: MutableList<Node<*>> = mutableListOf()
        val connections: MutableList<InputLinksState> = mutableListOf()

        fun add(node: Node<*>) { nodes.add(node) }

        fun remove(node: Node<*>) { nodes.remove(node) }

        fun findNodeById(id: UInt): Node<*>? {
            for(node in nodes) { if (node.id == id) { return node } }
            return null
        }

        fun getId(): UInt { return currentId.also { currentId += 1u } }

        fun save(): String {
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Scene::class.java, SceneSerializer())
                .registerTypeAdapter(NodeState::class.java, NodeSerializer())
                .create()
            return gson.toJson(this)
        }

        fun load(json: String): Scene {
            val gson = GsonBuilder()
                .registerTypeAdapter(Scene::class.java, SceneDeserializer(nodeState, linkState))
                .registerTypeAdapter(Node::class.java, NodeDeserializer(nodeState, linkState))
                .create()
            return gson.fromJson(json, Scene::class.java)
        }
    }

data class InputLinksState (
    val id: Int,
    val connections: MutableList<LinkKey<Int, Int>>
): Serializable

class NodeState(node: Node<*>) {
    val position: Bounds = node.boundsInParent
    val id = node.id
    val type = node.type
    val value = node.value
}

fun JsonArray.toByteArray(): ByteArray {
    val byteArray = ByteArray(size())
    for(i in 0 until size()) {
        byteArray[i] = (get(i).asInt and 0xFF).toByte()
    }
    return byteArray
}

class NodeDeserializer(
    val nodeState: DataFormat,
    val linkState: DataFormat,
): JsonDeserializer<Node<*>>
{
    override fun deserialize(json: JsonElement?,
                             typeOfT: Type?,
                             context: JsonDeserializationContext?): Node<*>? {
        val jsonObject = json?.asJsonObject

        return jsonObject?.let { jo ->
            val id = jo.get("id").asInt.toUInt()
            val x = jo.get("x").asDouble
            val y = jo.get("y").asDouble

            when(jo.get("type").asString) {
                IntNodeType -> {
                    val value = jo.get("value").asInt
                    IntValue(nodeState, linkState, id).also { it.load(x, y, value) }
                }
                FloatNodeType -> {
                    val value = jo.get("value").asFloat
                    FloatValue(nodeState, linkState, id).also { it.load(x, y, value) }
                }
                StringNodeType -> {
                    val value = jo.get("value").asString
                    StringValue(nodeState, linkState, id).also {it.load(x, y, value)}
                }
                ImageNodeType ->  {
                    val value = jo.get("value")
                    val bufImage: BufferedImage? = if (value == null) null
                    else ImageIO.read(ByteArrayInputStream(jo.getAsJsonArray("value").toByteArray()))
                    ImageNode(nodeState, linkState, id).also { it.load(x, y, bufImage) }
                }
                EndNodeType -> { EndNode(nodeState, linkState, id).also { it.load(x, y, null) } }
                StartNodeType -> {
                    val value = jo.get("value")
                    val bufImage: BufferedImage? = if (value == null) null
                    else ImageIO.read(ByteArrayInputStream(jo.getAsJsonArray("value").toByteArray()))
                    StartNode(nodeState, linkState, id).also { it.load(x, y, bufImage) } }

                AddImageNodeType -> { AddImage(nodeState, linkState, id).also { it.load(x, y, null) } }
                AddTextNodeType -> { AddText(nodeState, linkState, id).also { it.load(x, y, null) } }
                BlurNodeType -> { Blur(nodeState, linkState, id).also { it.load(x, y, null) } }
                BrightnessNodeType -> { Brightness(nodeState, linkState, id).also { it.load(x, y, null) } }
                GrayFilterNodeType -> { Gray(nodeState, linkState, id).also { it.load(x, y, null) } }
                InvertNodeType -> { Invert(nodeState, linkState, id).also { it.load(x, y, null) } }
                MoveNodeType -> { Move(nodeState, linkState, id).also { it.load(x, y, null) } }
                RotationNodeType -> { Rotation(nodeState, linkState, id).also { it.load(x, y, null) } }
                ScaleNodeType -> { Scale(nodeState, linkState, id).also { it.load(x, y, null) } }
                SepiaNodeType -> { Sepia(nodeState, linkState, id).also { it.load(x, y, null) } }
                else -> null
            }

        }

    }
}

class NodeSerializer: JsonSerializer<NodeState> {
    override fun serialize(src: NodeState?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val result = JsonObject()
        if (src == null) return result

        val position = src.position
        result.addProperty("x", position.minX)
        result.addProperty("y", position.minY)
        result.addProperty("id", src.id.toString())
        result.addProperty("type", src.type)


        if(src.value is BufferedImage?) {
            var writableImage: ByteArray? = null
            if (src.value != null) {
                val baos = ByteArrayOutputStream()
                ImageIO.write(src.value, "png", baos)
                writableImage = baos.toByteArray()
            }
            result.add("value", context!!.serialize(writableImage))
        }
        if (src.value is Int || src.value is String || src.value is Float) { result.addProperty("value", src.value.toString()) }

        return result
    }
}

class SceneDeserializer(val nodeState: DataFormat, val linkState: DataFormat): JsonDeserializer<Scene> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Scene? {
        val jsonObject = json?.asJsonObject
        if(context == null || json == null) return null

        return jsonObject?.let { jo ->
            val currentId = jo.get("currentId").asInt.toUInt()
            val scene = Scene(nodeState, linkState, currentId)
            val nodes = jo.get("nodes").asJsonArray
            for(node in nodes) { scene.add(context.deserialize(node, Node::class.java)) }

            val jsonConnections = jo.getAsJsonArray("connections")

            for (jsonNodeConnections in jsonConnections) {
                scene.connections.add(context.deserialize(jsonNodeConnections, InputLinksState::class.java))
                println(jsonConnections)
            }
            scene
        }
    }
}

class SceneSerializer: JsonSerializer<Scene> {
    override fun serialize(src: Scene?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val result = JsonObject()

        if (src == null || context == null) return result

        val nodes = JsonArray()
        val connections = JsonArray()
        for (node in src.nodes) {
            val nodeState = NodeState(node)
            val serializedNode = context.serialize(nodeState)
            nodes.add(serializedNode)

            val nodeConnections = node.connectedLinks
            for (connection in nodeConnections) {
                val currentConnections = mutableListOf<LinkKey<Int, Int>>()
                currentConnections.add(LinkKey(connection.source.id.toInt(), node.linkInputs.indexOf(connection.destination)))
                connections.add(context.serialize(InputLinksState(node.id.toInt(), currentConnections)))
            }
        }

        result.addProperty("currentId", src.getId().toInt())
        result.add("nodes", nodes)
        result.add("connections", connections)

        return result
    }
}