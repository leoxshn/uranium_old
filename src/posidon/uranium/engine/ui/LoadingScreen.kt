package posidon.uranium.engine.ui;

import org.lwjgl.opengl.GL11;
import posidon.uranium.engine.Window;
import posidon.uranium.engine.graphics.Shader;
import posidon.uranium.engine.graphics.Texture;
import posidon.uranium.engine.maths.Matrix4f;
import posidon.uranium.engine.maths.Vec2f;
import posidon.uranium.engine.maths.Vec3f;

public class LoadingScreen extends View {

    @Override
    public void setBackgroundPath(String path) {
        super.setBackgroundPath(path);
    }

    public LoadingScreen() {super(
            new Vec2f(0, 0),
            new Vec2f(2, 2),
            new Texture("res/textures/ui/loading.png")
    );}

    public void render(Shader shader) {
        background.bind();
        shader.setUniform("ambientLight", new Vec3f(1, 1, 1));
        if (Window.width() > Window.height())
            shader.setUniform("model", Matrix4f.transform(position, new Vec2f(size.x / Window.width() * Window.height(), size.y)));
        else shader.setUniform("model", Matrix4f.transform(position, new Vec2f(size.x, size.y / Window.height() * Window.width())));
        GL11.glDrawElements(GL11.GL_TRIANGLES, getMESH().vertexCount, GL11.GL_UNSIGNED_INT, 0);
    }
}
