package posidon.uranium.engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.system.MemoryUtil
import posidon.uranium.engine.maths.Matrix4f
import posidon.uranium.engine.maths.Vec2f
import posidon.uranium.engine.maths.Vec3f
import posidon.uranium.engine.utils.FileUtils

class Shader(vertexPath: String, fragmentPath: String) {

    private val vertexFile: String?
    private val fragmentFile: String?
    private var vertexID = 0
    private var fragmentID = 0
    private var programID = 0

    fun create() {
        programID = GL20.glCreateProgram()
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        GL20.glShaderSource(vertexID, vertexFile)
        GL20.glCompileShader(vertexID)
        if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(vertexID))
            return
        }
        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        GL20.glShaderSource(fragmentID, fragmentFile)
        GL20.glCompileShader(fragmentID)
        if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(fragmentID))
            return
        }
        GL20.glAttachShader(programID, vertexID)
        GL20.glAttachShader(programID, fragmentID)
        GL20.glLinkProgram(programID)
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(programID))
        GL20.glValidateProgram(programID)
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE)
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(programID))
    }

    fun getUniformLocation(name: String?) = GL20.glGetUniformLocation(programID, name)

    fun setUniform(name: String?, value: Float) = GL20.glUniform1f(getUniformLocation(name), value)
    fun setUniform(name: String?, value: Int) = GL20.glUniform1i(getUniformLocation(name), value)
    fun setUniform(name: String?, value: Boolean) = GL20.glUniform1i(getUniformLocation(name), if (value) 1 else 0)
    fun setUniform(name: String?, value: Vec2f) = GL20.glUniform2f(getUniformLocation(name), value.x, value.y)
    fun setUniform(name: String?, value: Vec3f?) =
            GL20.glUniform3f(getUniformLocation(name), value!!.x, value.y, value.z)
    fun setUniform(name: String?, value: Matrix4f) {
        val matrix = MemoryUtil.memAllocFloat(Matrix4f.Companion.SIZE * Matrix4f.Companion.SIZE)
        matrix.put(value.all).flip()
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix)
    }

    fun bind() = GL20.glUseProgram(programID)
    fun unbind() = GL20.glUseProgram(0)

    fun kill() {
        GL20.glDetachShader(programID, vertexID)
        GL20.glDetachShader(programID, fragmentID)
        GL20.glDeleteShader(vertexID)
        GL20.glDeleteShader(fragmentID)
        GL20.glDeleteProgram(programID)
    }

    init {
        vertexFile = FileUtils.loadAsString(vertexPath)
        fragmentFile = FileUtils.loadAsString(fragmentPath)
    }
}