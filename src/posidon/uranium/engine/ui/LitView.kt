package posidon.uranium.engine.ui

import org.lwjgl.opengl.GL11
import posidon.uranium.engine.Window
import posidon.uranium.engine.graphics.Shader
import posidon.uranium.engine.graphics.Texture
import posidon.uranium.engine.maths.Matrix4f
import posidon.uranium.engine.maths.Vec2f
import posidon.uranium.engine.maths.Vec3f

abstract class LitView(position: Vec2f, size: Vec2f, background: Texture) : View(position, size, background) {
    override fun render(shader: Shader?) {
        background.bind()
        shader!!.setUniform("ambientLight", Vec3f(1f, 1f, 1f))
        shader.setUniform("model", Matrix4f.transform(position, Vec2f(size.x / Window.width * Window.height, size.y)))
        GL11.glDrawElements(GL11.GL_TRIANGLES, MESH.vertexCount, GL11.GL_UNSIGNED_INT, 0)
    }
}