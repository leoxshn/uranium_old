package posidon.uranium.engine;

import posidon.uranium.engine.maths.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import posidon.uranium.main.Globals;

import static org.lwjgl.opengl.GL11.GL_TRUE;

public class Window {
    private static int width, height;
    private static String title;
    private long window;
    private Input input;
    private GLFWWindowSizeCallback resizeListener;
    private boolean fullscreen;
    private int[] pos = new int[2];
    private static Matrix4f projection;
    static boolean mouseLocked;

    public Window(int width, int height, String title) {
        Window.width = width;
        Window.height = height;
        Window.title = title;
        projection = Matrix4f.projection(70f, (float)width/(float)height, 0.2f, 200);
    }

    public void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("[ERROR]: GLFW wasn't inititalized");
            return;
        }

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        input = new Input(this);
        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            System.err.println("[ERROR]: Window wasn't created");
            return;
        }

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        pos[0] = (videoMode.width() - width) / 2;
        pos[1] = (videoMode.height() - height) / 2;
        GLFW.glfwSetWindowPos(window, pos[0], pos[1]);
        GLFW.glfwSetWindowSizeLimits(window, 600, 300, -1, -1);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        createCallbacks();

        GLFW.glfwShowWindow(window);
        GLFW.glfwSwapInterval(1);
    }

    private void createCallbacks() {
        resizeListener = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                projection = Matrix4f.projection(70f, (float)width/(float)height, 0.2f, 400);
                GL11.glViewport(0, 0, width, height);
            }
        };
        GLFW.glfwSetKeyCallback(window, input.getKeyListener());
        GLFW.glfwSetCursorPosCallback(window, input.getCursorListener());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonListener());
        GLFW.glfwSetScrollCallback(window, input.getScrollListener());
        GLFW.glfwSetWindowSizeCallback(window, resizeListener);
    }

    public void update() {
        GL11.glClearColor(Globals.skyColor.x, Globals.skyColor.y, Globals.skyColor.z, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();
    }
    public void swapBuffers() { GLFW.glfwSwapBuffers(window); }
    public boolean shouldClose() { return GLFW.glfwWindowShouldClose(window); }
    public void kill() {
        input.kill();
        resizeListener.free();
        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public void setMouseLocked(boolean lock) {
        if (lock) GLFW.glfwSetCursorPos(window, Input.getCurX(), Input.getCurY());
        mouseLocked = lock;
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }

    public static int width() { return width; }
    public static int height() { return height; }
    public boolean isFullscreen() { return fullscreen; }
    public void setFullscreen(boolean fullscreen) {
        if (fullscreen) {
            GLFW.glfwGetWindowPos(window, new int[]{pos[0]}, new int[]{pos[1]});
            GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), 0);
        } else GLFW.glfwSetWindowMonitor(window, 0, pos[0], pos[1], width, height, 0);
        this.fullscreen = fullscreen;
    }
    public static Matrix4f getProjectionMatrix() {
        return (Input.isKeyDown(GLFW.GLFW_KEY_C)) ? Matrix4f.projection(20f, (float)width/(float)height, 0.2f, 200) : projection;
    }
}
