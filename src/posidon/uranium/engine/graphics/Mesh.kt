package posidon.uranium.engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.util.*

class Mesh {

    var vaoId = 0
    var vertexCount = 0
    private var vboIdList = ArrayList<Int>()

    constructor(positions: FloatArray, indices: IntArray, uv: FloatArray) {
        var posBuffer: FloatBuffer? = null
        var textCoordsBuffer: FloatBuffer? = null
        var indicesBuffer: IntBuffer? = null
        try {
            vertexCount = indices.size
            vaoId = GL30.glGenVertexArrays()
            GL30.glBindVertexArray(vaoId)
            // Position VBO
            var vboId = GL15.glGenBuffers()
            vboIdList.add(vboId)
            posBuffer = MemoryUtil.memAllocFloat(positions.size)
            posBuffer.put(positions).flip()
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, posBuffer, GL15.GL_STATIC_DRAW)
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)
            // Texture coordinates VBO
            vboId = GL15.glGenBuffers()
            vboIdList.add(vboId)
            textCoordsBuffer = MemoryUtil.memAllocFloat(uv.size)
            textCoordsBuffer.put(uv).flip()
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textCoordsBuffer, GL15.GL_STATIC_DRAW)
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0)
            // Index VBO
            vboId = GL15.glGenBuffers()
            vboIdList.add(vboId)
            indicesBuffer = MemoryUtil.memAllocInt(indices.size)
            indicesBuffer.put(indices).flip()
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId)
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
            GL30.glBindVertexArray(0)
        } finally {
            if (posBuffer != null) MemoryUtil.memFree(posBuffer)
            if (textCoordsBuffer != null) MemoryUtil.memFree(textCoordsBuffer)
            if (indicesBuffer != null) MemoryUtil.memFree(indicesBuffer)
        }
    }

    constructor(vertices: FloatArray, uv: FloatArray) {
        var posBuffer: FloatBuffer? = null
        var textCoordsBuffer: FloatBuffer? = null
        try {
            vertexCount = vertices.size
            vboIdList = ArrayList()
            vaoId = GL30.glGenVertexArrays()
            GL30.glBindVertexArray(vaoId)
            var vboId = GL15.glGenBuffers()
            vboIdList.add(vboId)
            posBuffer = MemoryUtil.memAllocFloat(vertices.size)
            posBuffer.put(vertices).flip()
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, posBuffer, GL15.GL_STATIC_DRAW)
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)
            vboId = GL15.glGenBuffers()
            vboIdList.add(vboId)
            textCoordsBuffer = MemoryUtil.memAllocFloat(uv.size)
            textCoordsBuffer.put(uv).flip()
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textCoordsBuffer, GL15.GL_STATIC_DRAW)
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
            GL30.glBindVertexArray(0)
        } finally {
            if (posBuffer != null) MemoryUtil.memFree(posBuffer)
            if (textCoordsBuffer != null) MemoryUtil.memFree(textCoordsBuffer)
        }
    }

    fun getVbo(i: Int): Int = vboIdList[i]

    fun delete() {
        GL20.glDisableVertexAttribArray(0)
        // Delete the VBOs
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        for (vboId in vboIdList) GL15.glDeleteBuffers(vboId)
        // Delete the VAO
        GL30.glBindVertexArray(0)
        GL30.glDeleteVertexArrays(vaoId)
    }
}