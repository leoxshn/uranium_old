package posidon.uranium.engine.objects

import posidon.potassium.universe.block.Block
import posidon.uranium.content.Textures
import posidon.uranium.engine.graphics.Mesh
import posidon.uranium.engine.graphics.Renderer
import posidon.uranium.engine.maths.Vec3i
import java.util.*

class Cube(block: Block, var positionInChunk: Vec3i, var chunkPos: Vec3i) {

    val absolutePosition inline get() = chunkPos * Chunk.CHUNK_SIZE + positionInChunk
    var name: String = block.name
    var hardness: Float = block.hardness
    var emission: Float = block.emission

    val mesh get() = meshes[sides]
    val texture get() = Textures.blocks[name]

    override fun hashCode() =
            Objects.hash(chunkPos, positionInChunk, name, hardness, emission) + sides.contentHashCode()

    var sides = BooleanArray(6)
        set(value) {
            field = value
            val cubesBySides = chunk!!.cubesBySides
            if (cubesBySides.containsKey(sides)) cubesBySides[sides]!!.remove(this)
            cubesBySides.computeIfAbsent(sides) { ArrayList() }
            cubesBySides[sides]!!.add(this)
            if (!meshes.containsKey(sides)) {
                val ints = ArrayList<Int>()
                run {
                    var i = 0
                    while (i < 36) {
                        if (sides[i / 6]) {
                            ints.add(indicesTemplate[i++])
                            ints.add(indicesTemplate[i++])
                            ints.add(indicesTemplate[i++])
                            ints.add(indicesTemplate[i++])
                            ints.add(indicesTemplate[i++])
                            ints.add(indicesTemplate[i++])
                        } else i += 6
                    }
                }
                val indices = IntArray(ints.size)
                for (i in ints.indices) indices[i] = ints[i]
                ints.clear()
                val newMesh = Mesh(vertices, indices, uv)
                meshes[field] = newMesh
            }
        }

    val chunk get() = Renderer.chunks[chunkPos]

    fun update() {
        try {
            val s = BooleanArray(6)
            s[2] = positionInChunk.x == Chunk.CHUNK_SIZE - 1 || chunk!![positionInChunk.x + 1, positionInChunk.y, positionInChunk.z] == null
            s[3] = positionInChunk.x == 0 || chunk!![positionInChunk.x - 1, positionInChunk.y, positionInChunk.z] == null
            s[1] = positionInChunk.y == Chunk.CHUNK_SIZE - 1 || chunk!![positionInChunk.x, positionInChunk.y + 1, positionInChunk.z] == null
            s[4] = positionInChunk.y == 0 || chunk!![positionInChunk.x, positionInChunk.y - 1, positionInChunk.z] == null
            s[0] = positionInChunk.z == Chunk.CHUNK_SIZE - 1 || chunk!![positionInChunk.x, positionInChunk.y, positionInChunk.z + 1] == null
            s[5] = positionInChunk.z == 0 || chunk!![positionInChunk.x, positionInChunk.y, positionInChunk.z - 1] == null
            sides = s
        } catch (e: Exception) {
            e.printStackTrace()
            println("something weird going on here!")
            sides = booleanArrayOf(true, true, true, true, true, true)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Cube
        if (chunkPos != other.chunkPos) return false
        if (positionInChunk != other.positionInChunk) return false
        if (name != other.name) return false
        if (hardness != other.hardness) return false
        if (emission != other.emission) return false
        return true
    }

    companion object {

        var meshes = HashMap<BooleanArray, Mesh?>()

        fun kill() = meshes.clear()

        private val indicesTemplate = intArrayOf(
                0, 1, 3, 3, 1, 2,  // Front
                8, 10, 11, 9, 8, 11,  // Top
                12, 13, 7, 5, 12, 7,  // Right
                6, 14, 4, 6, 15, 14,  // Left
                16, 18, 19, 17, 16, 19,  // Bottom
                7, 4, 5, 7, 6, 4 // Back
        )
        private val vertices = floatArrayOf(
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f
        )
        private val uv = floatArrayOf(
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,  // For text coords in top face
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,  // For text coords in right face
                0.0f, 0.0f,
                0.0f, 1.0f,  // For text coords in left face
                1.0f, 0.0f,
                1.0f, 1.0f,  // For text coords in bottom face
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
        )
    }

    init { update() }
}