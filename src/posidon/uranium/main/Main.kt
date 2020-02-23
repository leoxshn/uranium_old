package posidon.uranium.main

import posidon.uranium.client.Client
import posidon.uranium.content.Textures
import posidon.uranium.engine.Window
import posidon.uranium.engine.graphics.Renderer
import posidon.uranium.engine.maths.Vec2f
import posidon.uranium.engine.maths.Vec3f
import posidon.uranium.engine.objects.Camera
import posidon.uranium.engine.ui.HotBar
import posidon.uranium.engine.ui.LoadingScreen
import kotlin.system.exitProcess

class Main : Runnable {

    private var window: Window? = null
    private val camera = Camera(Vec3f(0f, 0f, 0f), Vec2f(0f, 0f))
    var ns = 1000000000 / 60.0

    fun start(width: Int, height: Int) {
        window = Window(width, height, "uranium")
        Thread(this, "uranium").start()
    }

    override fun run() { ////START/////////////////////////////////////
        window!!.create()
        Renderer.init()
        val loadingScreen = LoadingScreen()
        render()
        val success: Boolean = Client.start("localhost", 2512)
        if (!success) {
            loadingScreen.setBackgroundPath("res/textures/ui/couldnt_connect.png")
            while (window!!.isOpen) render()
            kill()
        }
        Textures.set(null)
        var lastTime = System.nanoTime()
        var delta = 0.0
        Thread(BackgroundThread()).start()
        Thread(CameraThread()).start()
        Renderer.updateBlocks()
        loadingScreen.visible = false
        ////GUI///////////////////////////////////////
        HotBar()
        ////LOOP//////////////////////////////////////
        while (window!!.isOpen && running) {
            val now = System.nanoTime()
            delta += (now - lastTime) / ns
            lastTime = now
            render()
            if (delta > 6) {
                Renderer.updateBlocks()
                delta = 0.0
            }
        }
        kill()
        //////////////////////////////////////////////
    }

    private inner class BackgroundThread : Runnable {
        override fun run() {
            var lastTime = System.nanoTime()
            var delta = 0.0
            while (running) {
                val now = System.nanoTime()
                delta += (now - lastTime) / ns
                lastTime = now
                while (delta >= 1) {
                    Renderer.bg()
                    Globals.tick()
                    delta--
                }
            }
        }
    }

    private inner class CameraThread : Runnable {
        override fun run() {
            var lastTime = System.nanoTime()
            var delta = 0.0
            while (running) {
                val now = System.nanoTime()
                delta += (now - lastTime) / ns
                lastTime = now
                while (delta >= 1) {
                    camera.tick()
                    delta--
                }
            }
        }
    }

    private fun render() {
        window!!.update()
        Renderer.render()
        window!!.swapBuffers()
    }

    private fun kill() {
        running = false
        window!!.kill()
        Renderer.kill()
        Client.kill()
        exitProcess(0)
    }

    companion object {
        var running = true
    }
}