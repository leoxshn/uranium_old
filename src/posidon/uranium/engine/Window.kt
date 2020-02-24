package posidon.uranium.engine

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import posidon.uranium.engine.maths.Matrix4f
import posidon.uranium.main.Globals

class Window(width: Int, height: Int, title: String?) {

    private var window: Long = 0
    private var input: Input? = null
    private var resizeListener: GLFWWindowSizeCallback? = null

    var isFullscreen = false
        set(fullscreen) {
            if (fullscreen) {
                GLFW.glfwGetWindowPos(window, intArrayOf(pos[0]), intArrayOf(pos[1]))
                val videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
                GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, videoMode!!.width(), videoMode.height(), 0)
            } else GLFW.glfwSetWindowMonitor(window, 0, pos[0], pos[1], width, height, 0)
            field = fullscreen
        }
    private val pos = IntArray(2)

    fun create() {
        if (!GLFW.glfwInit()) {
            System.err.println("[ERROR]: GLFW wasn't inititalized")
            return
        }
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE)
        input = Input(this)
        window = GLFW.glfwCreateWindow(width, height, title, 0, 0)
        if (window == 0L) {
            System.err.println("[ERROR]: Window wasn't created")
            return
        }
        val videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
        pos[0] = (videoMode!!.width() - width) / 2
        pos[1] = (videoMode.height() - height) / 2
        GLFW.glfwSetWindowPos(window, pos[0], pos[1])
        GLFW.glfwSetWindowSizeLimits(window, 600, 300, -1, -1)
        GLFW.glfwMakeContextCurrent(window)
        GL.createCapabilities()
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        createCallbacks()
        GLFW.glfwShowWindow(window)
        GLFW.glfwSwapInterval(1)
    }

    private fun createCallbacks() {
        resizeListener = object : GLFWWindowSizeCallback() {
            override fun invoke(window: Long, w: Int, h: Int) {
                width = w
                height = h
                projection = Matrix4f.projection(70f, width.toFloat() / height.toFloat(), 0.2f, 400f)
                GL11.glViewport(0, 0, width, height)
            }
        }
        GLFW.glfwSetKeyCallback(window, input!!.keyListener)
        GLFW.glfwSetCursorPosCallback(window, input!!.cursorListener)
        GLFW.glfwSetMouseButtonCallback(window, input!!.mouseButtonListener)
        GLFW.glfwSetScrollCallback(window, input!!.scrollListener)
        GLFW.glfwSetWindowSizeCallback(window, resizeListener)
    }

    fun update() {
        GL11.glClearColor(Globals.skyColor.x, Globals.skyColor.y, Globals.skyColor.z, 1.0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GLFW.glfwPollEvents()
    }

    fun swapBuffers() = GLFW.glfwSwapBuffers(window)

    val isOpen get() = !GLFW.glfwWindowShouldClose(window)

    fun kill() {
        input!!.kill()
        resizeListener!!.free()
        GLFW.glfwDestroyWindow(window)
        GLFW.glfwTerminate()
    }

    fun setMouseLocked(lock: Boolean) {
        if (lock) GLFW.glfwSetCursorPos(window, Input.curX, Input.curY)
        mouseLocked = lock
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, if (lock) GLFW.GLFW_CURSOR_DISABLED else GLFW.GLFW_CURSOR_NORMAL)
    }

    companion object {
        var width = 0; private set
        var height = 0; private set
        private var title: String? = null
        private lateinit var projection: Matrix4f
        var mouseLocked = false

        val projectionMatrix: Matrix4f
            get() = if (Input.isKeyDown(GLFW.GLFW_KEY_C)) Matrix4f.projection(20f, width.toFloat() / height.toFloat(), 0.2f, 200f) else projection
    }

    init {
        Companion.width = width
        Companion.height = height
        Companion.title = title
        projection = Matrix4f.projection(70f, width.toFloat() / height.toFloat(), 0.2f, 200f)
    }
}