package posidon.uranium.engine.ui;

import org.lwjgl.opengl.GL11;
import posidon.uranium.engine.Window;
import posidon.uranium.engine.graphics.Mesh;
import posidon.uranium.engine.graphics.Renderer;
import posidon.uranium.engine.graphics.Shader;
import posidon.uranium.engine.graphics.Texture;
import posidon.uranium.engine.maths.Matrix4f;
import posidon.uranium.engine.maths.Vec2f;
import posidon.uranium.main.Globals;

public abstract class View {

    private static Mesh MESH;

    public Vec2f position;
    public Vec2f size;
    protected Texture background;
    public boolean visible = true;

    View(Vec2f position, Vec2f size, Texture background) {
        this.position = position;
        this.size = size;
        this.background = background;
        Renderer.ui.add(this);
    }

    public void setBackgroundPath(String path) {
        background.delete();
        background = new Texture(path);
    }

    public void destroy() {
        Renderer.ui.remove(this);
        background.delete();
    }

    public static Mesh getMESH() { return MESH; }

    public static void init() {
        MESH = new Mesh(new float[] {
                /*V0*/ -0.5f, 0.5f, 0,
                /*V1*/ -0.5f, -0.5f, 0,
                /*V2*/ 0.5f, -0.5f, 0,
                /*V3*/ 0.5f, 0.5f, 0
        }, new int[] { 0, 1, 3, 3, 1, 2 }, new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f});
    }

    public void render(Shader shader) {
        background.bind();
        shader.setUniform("ambientLight", Globals.ambientLight);
        shader.setUniform("model", Matrix4f.transform(position, new Vec2f(size.x / Window.width() * Window.height(), size.y)));
        GL11.glDrawElements(GL11.GL_TRIANGLES, MESH.vertexCount, GL11.GL_UNSIGNED_INT, 0);
    }
}
