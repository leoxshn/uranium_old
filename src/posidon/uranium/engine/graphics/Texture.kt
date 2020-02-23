package posidon.uranium.engine.graphics;

import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    public final int id;

    public Texture(String filename) {
        id = loadTexture(filename);
    }

    public void bind() { glBindTexture(GL_TEXTURE_2D, id); }

    private static int loadTexture(String path) {
        int width, height;
        ByteBuffer buf;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(path, w, h, channels, 4);
            //if (buf == null) throw new NoSuchFileException("Texture not loaded: [" + path + "] " + stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        //glGenerateMipmap(GL_TEXTURE_2D);
        stbi_image_free(buf);
        return textureId;
    }

    public void delete() { glDeleteTextures(id); }
}
