package posidon.uranium.engine.objects

import org.lwjgl.glfw.GLFW
import posidon.potassium.packets.Packet
import posidon.uranium.client.Client
import posidon.uranium.engine.Input
import posidon.uranium.engine.graphics.Renderer
import posidon.uranium.engine.maths.Matrix4f
import posidon.uranium.engine.maths.Vec2f
import posidon.uranium.engine.maths.Vec3f
import kotlin.math.cos
import kotlin.math.sin

class Camera(position: Vec3f?, rotation: Vec2f?) {

    private var oldCurX = 0.0
    private var oldCurY = 0.0

    fun tick() {
        val curX: Double = Input.curX
        val curY: Double = Input.curY
        var packet: Packet? = null
        var movX = 0f
        var movZ = 0f
        val keys = booleanArrayOf(
                Input.isKeyDown(GLFW.GLFW_KEY_W),
                Input.isKeyDown(GLFW.GLFW_KEY_S),
                Input.isKeyDown(GLFW.GLFW_KEY_A),
                Input.isKeyDown(GLFW.GLFW_KEY_D)
        )
        val dx = (curX - oldCurX).toFloat()
        val dy = (curY - oldCurY).toFloat()
        rotation = rotation!! + Vec2f(-sensitivity * dy, -sensitivity * dx)
        if (rotation!!.x > 90) rotation!!.x = 90f else if (rotation!!.x < -90) rotation!!.x = -90f
        if (rotation!!.y > 360) rotation!!.y -= 360f else if (rotation!!.y < 0) rotation!!.y += 360f
        if (keys[0]) {
            packet = Packet()
            movX = sin(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
            movZ = cos(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
            position!!.x -= movX
            position!!.z -= movZ
            packet["walk"] = rotation!!.y
            packet["keys"] = keys
        }
        if (keys[1]) {
            if (packet == null) {
                packet = Packet()
                movX = sin(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
                movZ = cos(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
            }
            position!!.x += movX
            position!!.z += movZ
            packet["walk"] = rotation!!.y
            packet["keys"] = keys
        }
        if (keys[2]) {
            if (packet == null) {
                packet = Packet()
                movX = sin(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
                movZ = cos(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
            }
            position!!.x -= movZ
            position!!.z += movX
            packet["walk"] = rotation!!.y
            packet["keys"] = keys
        }
        if (keys[3]) {
            if (packet == null) {
                packet = Packet()
                movX = sin(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
                movZ = cos(Math.toRadians(rotation!!.y.toDouble())).toFloat() * moveSpeed
            }
            position!!.x += movZ
            position!!.z -= movX
            packet["walk"] = rotation!!.y
            packet["keys"] = keys
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            position!!.y += jumpHeight
            if (packet == null) packet = Packet()
            packet["fly"] = 1
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            position!!.y -= jumpHeight
            if (packet == null) {
                packet = Packet()
                packet["fly"] = -1
            } else if (packet.containsKey("fly")) packet.remove("fly") else packet["fly"] = -1
        }
        if (packet != null) Client.send(packet)
        oldCurX = curX
        oldCurY = curY
        Renderer.viewMatrix = Matrix4f.view(position, rotation)
    }

    companion object {
        var position: Vec3f? = null
        var rotation: Vec2f? = null
        var moveSpeed = 0f
        var jumpHeight = 0f
        var sensitivity = 0.4f
    }

    init {
        Companion.position = position
        Companion.rotation = rotation
    }
}