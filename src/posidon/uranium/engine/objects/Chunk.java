package posidon.uranium.engine.objects;

import posidon.uranium.engine.Window;
import posidon.uranium.engine.maths.Vec3i;

import java.util.HashMap;
import java.util.List;

public class Chunk {
    public static final int CHUNK_SIZE = 12;
    public static final int CHUNK_SIZE_CUBED = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE;
    private Cube[] cubes = new Cube[CHUNK_SIZE_CUBED];
    public Cube getCube(Vec3i pos) { return cubes[pos.x * CHUNK_SIZE * CHUNK_SIZE + pos.y * CHUNK_SIZE + pos.z]; }
    public Cube getCube(int x, int y, int z) { return cubes[x * CHUNK_SIZE * CHUNK_SIZE + y * CHUNK_SIZE + z]; }
    public void setCube(Cube cube, Vec3i pos) { cubes[pos.x * CHUNK_SIZE * CHUNK_SIZE + pos.y * CHUNK_SIZE + pos.z] = cube; }
    public Cube[] getCubes() { return cubes; }
    public final HashMap<boolean[], List<Cube>> cubesBySides = new HashMap<>();
    private Vec3i chunkPos;

    public Chunk(Vec3i chunkPos) {
        this.chunkPos = chunkPos;
    }

    public Vec3i getPosition() {
        return chunkPos;
    }

    public void clear() {
        cubesBySides.clear();
    }

    public boolean isInFOV() {
        Vec3i posRelToCam = Vec3i.subtract(Vec3i.multiply(chunkPos, CHUNK_SIZE), Camera.position.toVec3i());
        float rotY = Camera.rotation.y - 180;
        double cosRY = Math.cos(Math.toRadians(rotY));
        double sinRY = Math.sin(Math.toRadians(rotY));
        double cosRX = Math.cos(Math.toRadians(Camera.rotation.x));
        double sinRX = Math.sin(Math.toRadians(Camera.rotation.x));
        double x = (posRelToCam.x * cosRY - posRelToCam.z * sinRY) * cosRX + posRelToCam.y * sinRX;
        double z = (posRelToCam.z * cosRY + posRelToCam.x * sinRY) * cosRX + posRelToCam.y * sinRX;
        double y = posRelToCam.y * cosRX - z * sinRX;
        double maxXOffset = z * Window.width() / Window.height() + CHUNK_SIZE * 2;
        double maxYOffset = z * cosRX + posRelToCam.y * sinRX + CHUNK_SIZE * 2;
        return z > -CHUNK_SIZE * 2 && x < maxXOffset && x > -maxXOffset && y < maxYOffset && y > -maxYOffset;
    }
}
