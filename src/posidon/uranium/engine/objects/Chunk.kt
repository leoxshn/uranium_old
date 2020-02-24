package posidon.uranium.engine.objects

import posidon.uranium.engine.Window
import posidon.uranium.engine.maths.Vec3i
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Chunk(val position: Vec3i?) {

    private val cubes = arrayOfNulls<Cube>(CHUNK_SIZE_CUBED)

    operator fun get(pos: Vec3i?) = cubes[pos!!.x * CHUNK_SIZE * CHUNK_SIZE + pos.y * CHUNK_SIZE + pos.z]
    operator fun get(x: Int, y: Int, z: Int) = cubes[x * CHUNK_SIZE * CHUNK_SIZE + y * CHUNK_SIZE + z]
    operator fun set(pos: Vec3i?, cube: Cube?) {
        cubes[pos!!.x * CHUNK_SIZE * CHUNK_SIZE + pos.y * CHUNK_SIZE + pos.z] = cube
    }

    val cubesBySides = HashMap<BooleanArray, MutableList<Cube>>()
    fun clear() = cubesBySides.clear()

    val isInFOV: Boolean
        get() {
            val posRelToCam: Vec3i = position!! * CHUNK_SIZE - Camera.position!!.toVec3i()
            val rotY: Float = Camera.rotation!!.y - 180
            val cosRY = cos(Math.toRadians(rotY.toDouble()))
            val sinRY = sin(Math.toRadians(rotY.toDouble()))
            val cosRX = cos(Math.toRadians(Camera.rotation!!.x.toDouble()))
            val sinRX = sin(Math.toRadians(Camera.rotation!!.x.toDouble()))
            val x = (posRelToCam.x * cosRY - posRelToCam.z * sinRY) * cosRX + posRelToCam.y * sinRX
            val z = (posRelToCam.z * cosRY + posRelToCam.x * sinRY) * cosRX + posRelToCam.y * sinRX
            val y = posRelToCam.y * cosRX - z * sinRX
            val maxXOffset: Double = z * Window.width / Window.height + CHUNK_SIZE * 2
            val maxYOffset = z * cosRX + posRelToCam.y * sinRX + CHUNK_SIZE * 2
            return z > -CHUNK_SIZE * 2 && x < maxXOffset && x > -maxXOffset && y < maxYOffset && y > -maxYOffset
        }

    companion object {
        const val CHUNK_SIZE = 12
        const val CHUNK_SIZE_CUBED = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE
    }
}