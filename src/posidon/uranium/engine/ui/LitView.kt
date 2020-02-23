package posidon.uranium.engine.ui;

import org.lwjgl.opengl.GL11;
import posidon.uranium.engine.Window;
import posidon.uranium.engine.graphics.Shader;
import posidon.uranium.engine.graphics.Texture;
import posidon.uranium.engine.maths.Matrix4f;
import posidon.uranium.engine.maths.Vec2f;
import posidon.uranium.engine.maths.Vec3f;

public abstract class LitView extends View {
    public LitView(Vec2f position, Vec2f size, Texture background) { super(position, size, background); }
    public void render(Shader shader) {
        background.bind();
        shader.setUniform("ambientLight", new Vec3f(1, 1, 1));
        shader.setUniform("model", Matrix4f.transform(position, new Vec2f(size.x / Window.width() * Window.height(), size.y)));
        GL11.glDrawElements(GL11.GL_TRIANGLES, getMESH().vertexCount, GL11.GL_UNSIGNED_INT, 0);
    }
}
