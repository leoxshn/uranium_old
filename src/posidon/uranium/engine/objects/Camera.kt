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
    public static float moveSpeed, jumpHeight, sensitivity = 0.4f;
    private double oldCurX, oldCurY;

    public Camera(Vec3f position, Vec2f rotation) { Camera.position = position; Camera.rotation = rotation; }

    public void tick() {
        double curX = Input.getCurX(), curY = Input.getCurY();
        Packet packet = null;
        float movX = 0, movZ = 0;
        boolean[] keys = {
                Input.isKeyDown(GLFW.GLFW_KEY_W),
                Input.isKeyDown(GLFW.GLFW_KEY_S),
                Input.isKeyDown(GLFW.GLFW_KEY_A),
                Input.isKeyDown(GLFW.GLFW_KEY_D)
        };

        float dx = (float) (curX - oldCurX);
        float dy = (float) (curY - oldCurY);

        rotation = Vec2f.sum(rotation, new Vec2f(-sensitivity * dy, -sensitivity * dx));
        if (rotation.x > 90) rotation.x = 90;
        else if (rotation.x < -90) rotation.x = -90;

        if (rotation.y > 360) rotation.y -= 360;
        else if (rotation.y < 0) rotation.y += 360;

        if (keys[0]) {
            packet = new Packet();
            movX = (float) Math.sin(Math.toRadians(rotation.y)) * moveSpeed;
            movZ = (float) Math.cos(Math.toRadians(rotation.y)) * moveSpeed;
            position.x -= movX;
            position.z -= movZ;
            packet.put("walk", rotation.y);
            packet.put("keys", keys);
        } if (keys[1]) {
            if (packet == null) {
                packet = new Packet();
                movX = (float) Math.sin(Math.toRadians(rotation.y)) * moveSpeed;
                movZ = (float) Math.cos(Math.toRadians(rotation.y)) * moveSpeed;
            }
            position.x += movX;
            position.z += movZ;
            packet.put("walk", rotation.y);
            packet.put("keys", keys);
        } if (keys[2]) {
            if (packet == null) {
                packet = new Packet();
                movX = (float) Math.sin(Math.toRadians(rotation.y)) * moveSpeed;
                movZ = (float) Math.cos(Math.toRadians(rotation.y)) * moveSpeed;
            }
            position.x -= movZ;
            position.z += movX;
            packet.put("walk", rotation.y);
            packet.put("keys", keys);
        } if (keys[3]) {
            if (packet == null) {
                packet = new Packet();
                movX = (float) Math.sin(Math.toRadians(rotation.y)) * moveSpeed;
                movZ = (float) Math.cos(Math.toRadians(rotation.y)) * moveSpeed;
            }
            position.x += movZ;
            position.z -= movX;
            packet.put("walk", rotation.y);
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
