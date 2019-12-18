package posidon.uranium.engine.objects;

import posidon.potassium.packets.Packet;
import posidon.uranium.client.Client;
import posidon.uranium.engine.Input;
import posidon.uranium.engine.graphics.Renderer;
import posidon.uranium.engine.maths.Matrix4f;
import posidon.uranium.engine.maths.Vec2f;
import posidon.uranium.engine.maths.Vec3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    public static Vec3f position;
    public static Vec2f rotation;
    public static float moveSpeed = 0.5f, jumpHeight = 0.5f, sensitivity = 0.4f;
    private double oldCurX, oldCurY;

    public Camera(Vec3f position, Vec2f rotation) { Camera.position = position; Camera.rotation = rotation; }

    public void tick() {
        double curX = Input.getCurX(), curY = Input.getCurY();
        Packet packet = null;
        float movX = (float) Math.sin(Math.toRadians(rotation.y)) * moveSpeed;
        float movZ = (float) Math.cos(Math.toRadians(rotation.y)) * moveSpeed;
        boolean[] keys = new boolean[4];

        float dx = (float) (curX - oldCurX);
        float dy = (float) (curY - oldCurY);

        rotation = Vec2f.sum(rotation, new Vec2f(-sensitivity * dy, -sensitivity * dx));
        if (rotation.x > 90) rotation.x = 90;
        else if (rotation.x < -90) rotation.x = -90;

        if (rotation.y > 360) rotation.y -= 360;
        else if (rotation.y < 0) rotation.y += 360;

        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            position.x -= movX;
            position.z -= movZ;
            packet = new Packet();
            packet.put("walk", rotation.y);
            keys[0] = true;
            packet.put("keys", keys);
        } if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
            position.x += movX;
            position.z += movZ;
            if (packet == null) packet = new Packet();
            packet.put("walk", rotation.y);
            keys[1] = true;
            packet.put("keys", keys);
        } if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            position.x -= movZ;
            position.z += movX;
            if (packet == null) packet = new Packet();
            packet.put("walk", rotation.y);
            keys[2] = true;
            packet.put("keys", keys);
        } if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            position.x += movZ;
            position.z -= movX;
            if (packet == null) packet = new Packet();
            packet.put("walk", rotation.y);
            keys[3] = true;
            packet.put("keys", keys);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            position.y += jumpHeight;
            if (packet == null) packet = new Packet();
            packet.put("fly", 1);
        } if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            position.y -= jumpHeight;
            if (packet == null) {
                packet = new Packet();
                packet.put("fly", -1);
            }
            else if (packet.containsKey("fly")) packet.remove("fly");
            else packet.put("fly", -1);
        }
        if (packet != null) Client.send(packet);
        oldCurX = curX;
        oldCurY = curY;
        Renderer.viewMatrix = Matrix4f.view(Camera.position, Camera.rotation);
    }
}
