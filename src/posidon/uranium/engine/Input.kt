package posidon.uranium.engine

import org.lwjgl.glfw.*

class Input(w: Window) {

    val keyListener: GLFWKeyCallback
    val cursorListener: GLFWCursorPosCallback
    val mouseButtonListener: GLFWMouseButtonCallback
    val scrollListener: GLFWScrollCallback

    fun kill() {
        keyListener.free()
        cursorListener.free()
        mouseButtonListener.free()
        scrollListener.free()
    }

    companion object {
        private val keys = BooleanArray(GLFW.GLFW_KEY_LAST)
        private val mouseButtons = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST)
        var curX = 0.0
        var curY = 0.0
        var scrollX = 0.0
        var scrollY = 0.0
        fun isKeyDown(key: Int): Boolean = keys[key]
        fun isButtonDown(btn: Int): Boolean = mouseButtons[btn]
    }

    init {
        keyListener = object : GLFWKeyCallback() {
            override fun invoke(window: Long, key: Int, scanCode: Int, action: Int, mods: Int) {
                try {
                    keys[key] = action != GLFW.GLFW_RELEASE
                    println(action)
                    when (key) {
                        GLFW.GLFW_KEY_F11 -> w.isFullscreen = !w.isFullscreen
                        GLFW.GLFW_KEY_ESCAPE -> w.setMouseLocked(false)
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
        }
        cursorListener = object : GLFWCursorPosCallback() {
            override fun invoke(window: Long, x: Double, y: Double) {
                if (Window.mouseLocked) {
                    curX = x
                    curY = y
                }
            }
        }
        mouseButtonListener = object : GLFWMouseButtonCallback() {
            override fun invoke(window: Long, btn: Int, action: Int, mods: Int) {
                mouseButtons[btn] = action != GLFW.GLFW_RELEASE
                if (btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) w.setMouseLocked(true)
            }
        }
        scrollListener = object : GLFWScrollCallback() {
            override fun invoke(window: Long, x: Double, y: Double) {
                scrollX += x
                scrollY += y
            }
        }
    }
}