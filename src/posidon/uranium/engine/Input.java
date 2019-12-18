package posidon.uranium.engine;

import org.lwjgl.glfw.*;

public class Input {
    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double curX, curY;
    private static double scrollX, scrollY;

    private GLFWKeyCallback keyListener;
    private GLFWCursorPosCallback cursorListener;
    private GLFWMouseButtonCallback mouseButtonListener;
    private GLFWScrollCallback scrollListener;

    public Input(Window w) {
        keyListener = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scanCode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);
                if (key == GLFW.GLFW_KEY_F11) w.setFullscreen(!w.isFullscreen());
                else if (key == GLFW.GLFW_KEY_ESCAPE) w.setMouseLocked(false);
            }
        };

        cursorListener = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if (Window.mouseLocked) {
                    curX = x;
                    curY = y;
                }
            }
        };

        mouseButtonListener = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int btn, int action, int mods) {
                mouseButtons[btn] = (action != GLFW.GLFW_RELEASE);
                if (btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) w.setMouseLocked(true);
            }
        };

        scrollListener = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                scrollX += x;
                scrollY += y;
            }
        };
    }

    public static boolean isKeyDown(int key) { return keys[key]; }
    public static boolean isButtonDown(int btn) { return mouseButtons[btn]; }

    public void kill() {
        keyListener.free();
        cursorListener.free();
        mouseButtonListener.free();
    }

    public static double getCurX() { return curX; }
    public static double getCurY() { return curY; }
    public static double getScrollX() { return scrollX; }
    public static double getScrollY() { return scrollY; }
    public GLFWKeyCallback getKeyListener() { return keyListener; }
    public GLFWCursorPosCallback getCursorListener() { return cursorListener; }
    public GLFWMouseButtonCallback getMouseButtonListener() { return mouseButtonListener; }
    public GLFWScrollCallback getScrollListener() { return scrollListener; }
}
