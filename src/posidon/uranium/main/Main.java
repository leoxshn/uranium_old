package posidon.uranium.main;

import posidon.uranium.client.Client;
import posidon.uranium.content.Textures;
import posidon.uranium.engine.graphics.Renderer;
import posidon.uranium.engine.maths.Vec2f;
import posidon.uranium.engine.objects.Camera;
import posidon.uranium.engine.Window;
import posidon.uranium.engine.maths.Vec3f;
import posidon.uranium.engine.ui.HotBar;
import posidon.uranium.engine.ui.LoadingScreen;

public class Main implements Runnable {

    private Window window;
    private Camera camera = new Camera(new Vec3f(0, 0, 0), new Vec2f(0, 0));
    public static boolean running = true;

    double ns = 1000000000 / 60.0;

    public void start(int width, int height) {
        window = new Window(width, height, "uranium");
        new Thread(this, "uranium").start();
    }

    public void run() {
        ////START/////////////////////////////////////
        window.create();
        Renderer.init();
        LoadingScreen loadingScreen = new LoadingScreen();
        render();
        boolean success = Client.start("localhost", 2512);
        if (!success) {
            loadingScreen.setBackgroundPath("res/textures/ui/couldnt_connect.png");
            while (window.isOpen()) render();
            kill();
        }
        Textures.set(null);
        long lastTime = System.nanoTime();
        double delta = 0;
        new Thread(new BackgroundThread()).start();
        new Thread(new CameraThread()).start();
        Renderer.updateBlocks();
        loadingScreen.visible = false;

        ////GUI///////////////////////////////////////
        new HotBar();

        ////LOOP//////////////////////////////////////
        while(window.isOpen() && running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            render();
            if (delta > 6) {
                Renderer.updateBlocks();
                delta = 0;
            }
        }
        kill();
        //////////////////////////////////////////////
    }

    private class BackgroundThread implements Runnable {
        @Override public void run() {
            long lastTime = System.nanoTime();
            double delta = 0;
            while(running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while(delta >= 1) {
                    Renderer.bg();
                    Globals.tick();
                    delta--;
                }
            }
        }
    }

    private class CameraThread implements Runnable {
        @Override public void run() {
            long lastTime = System.nanoTime();
            double delta = 0;
            while(running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while(delta >= 1) {
                    camera.tick();
                    delta--;
                }
            }
        }
    }

    private void render() {
        window.update();
        Renderer.render();
        window.swapBuffers();
    }

    private void kill() {
        Main.running = false;
        window.kill();
        Renderer.kill();
        Client.kill();
        System.exit(0);
    }
}