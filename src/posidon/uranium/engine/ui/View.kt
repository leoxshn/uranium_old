package posidon.uranium.engine.ui

import org.lwjgl.opengl.GL11
import posidon.uranium.engine.Window
import posidon.uranium.engine.graphics.Mesh
import posidon.uranium.engine.graphics.Renderer
import posidon.uranium.engine.graphics.Shader
import posidon.uranium.engine.graphics.Texture
import posidon.uranium.engine.maths.Matrix4f
import posidon.uranium.engine.maths.Vec2f
import posidon.uranium.main.Globals

abstract class View internal constructor(var position: Vec2f, var size: Vec2f, protected var background: Texture) {

    var visible = true

    open fun setBackgroundPath(path: String?) {
        background.delete()
        background = Texture(path)
    }

    fun destroy() {
        Renderer.ui.remove(this)
        background.delete()
    }

    open fun render(shader: Shader?) {
        background.bind()
        shader!!.setUniform("ambientLight", Globals.ambientLight)
        shader.setUniform("model", Matrix4f.transform(position, Vec2f(size.x / Window.width() * Window.height(), size.y)))
        GL11.glDrawElements(GL11.GL_TRIANGLES, MESH.vertexCount, GL11.GL_UNSIGNED_INT, 0)
    }

    companion object {
        lateinit var MESH: Mesh
            private set

        fun init() {
            MESH = Mesh(floatArrayOf( /*V0*/
                    -0.5f, 0.5f, 0f,  /*V1*/
                    -0.5f, -0.5f, 0f,  /*V2*/
                    0.5f, -0.5f, 0f,  /*V3*/
                    0.5f, 0.5f, 0f), intArrayOf(0, 1, 3, 3, 1, 2), floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f))
        }
    }

    init {
        Renderer.ui.add(this)
    }
}