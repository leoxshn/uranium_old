package posidon.uranium.engine.objects;

import posidon.potassium.universe.block.Block;
import posidon.uranium.content.Textures;
import posidon.uranium.engine.graphics.Mesh;
import posidon.uranium.engine.graphics.Renderer;
import posidon.uranium.engine.graphics.Texture;
import posidon.uranium.engine.maths.Vec2f;
import posidon.uranium.engine.maths.Vec3f;
import posidon.uranium.engine.maths.Vec3i;

import java.util.*;

import static posidon.uranium.engine.objects.Chunk.CHUNK_SIZE;

public class Cube {

    public static HashMap<String, Texture> textures = new HashMap<>();
    public static HashMap<boolean[], Mesh> meshes = new HashMap<>();

    public Vec3i positionInChunk;
    public Vec3i absolutePosition;
    public Vec3i chunkPos;
    public String name;
    public float hardness;
    public float emission;
    private boolean[] sides = new boolean[6];

    public Mesh getMesh() { return meshes.get(sides); }

    public Cube(Block block, Vec3i positionInChunk, Vec3i chunkPos) {
        this.name = block.name;
        this.positionInChunk = positionInChunk;
        this.chunkPos = chunkPos;
        this.absolutePosition = Vec3i.sum(Vec3i.multiply(chunkPos, CHUNK_SIZE), positionInChunk);
        this.emission = block.emission;
        this.hardness = block.hardness;
        update();
    }

    public Texture getTexture() { return Textures.blocks.get(name); }

    public static void kill() {
        meshes.clear();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(absolutePosition, name, hardness, emission);
        result = 31 * result + Arrays.hashCode(sides);
        return result;
    }

    private static final int[] indicesTemplate = new int[] {
            0, 1, 3, 3, 1, 2,        // Front
            8, 10, 11, 9, 8, 11,     // Top
            12, 13, 7, 5, 12, 7,     // Right
            6, 14, 4, 6, 15, 14,     // Left
            16, 18, 19, 17, 16, 19,  // Bottom
            7, 4, 5, 7, 6, 4         // Back
    };

    public void setSides(boolean[] sides) {
        HashMap<boolean[], List<Cube>> cubesBySides = getChunk().cubesBySides;
        if (cubesBySides.containsKey(this.sides)) cubesBySides.get(this.sides).remove(this);
        this.sides = sides;
        cubesBySides.computeIfAbsent(sides, k -> new ArrayList<>());
        cubesBySides.get(sides).add(this);
        if (!meshes.containsKey(sides)) {
            ArrayList<Integer> ints = new ArrayList<>();
            for (int i = 0; i < 36;) if (sides[i / 6]) {
                ints.add(indicesTemplate[i++]);
                ints.add(indicesTemplate[i++]);
                ints.add(indicesTemplate[i++]);
                ints.add(indicesTemplate[i++]);
                ints.add(indicesTemplate[i++]);
                ints.add(indicesTemplate[i++]);
            } else i += 6;
            int[] indices = new int[ints.size()];
            for (int i = 0; i < ints.size(); i++) indices[i] = ints.get(i);
            ints.clear();
            Mesh newMesh = new Mesh(vertices, indices, uv);
            meshes.put(sides, newMesh);
        }
    }
    public boolean[] getSides() { return sides; }

    public Chunk getChunk() { return Renderer.chunks.get(chunkPos); }

    public void update() {
        try {
            boolean[] s = new boolean[6];
            s[2] = (positionInChunk.x == CHUNK_SIZE - 1 || getChunk().getCube(positionInChunk.x + 1, positionInChunk.y, positionInChunk.z) == null);
            s[3] = (positionInChunk.x == 0 || getChunk().getCube(positionInChunk.x - 1, positionInChunk.y, positionInChunk.z) == null);
            s[1] = (positionInChunk.y == CHUNK_SIZE - 1 || getChunk().getCube(positionInChunk.x, positionInChunk.y + 1, positionInChunk.z) == null);
            s[4] = (positionInChunk.y == 0 || getChunk().getCube(positionInChunk.x, positionInChunk.y - 1, positionInChunk.z) == null);
            s[0] = (positionInChunk.z == CHUNK_SIZE - 1 || getChunk().getCube(positionInChunk.x, positionInChunk.y, positionInChunk.z + 1) == null);
            s[5] = (positionInChunk.z == 0 || getChunk().getCube(positionInChunk.x, positionInChunk.y, positionInChunk.z - 1) == null);
            setSides(s);
        } catch (Exception e) {
            System.out.println("something weird going on here!");
            setSides(new boolean[]{true, true, true, true, true, true});
        }
    }

    private static final float[] vertices = new float[] {
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f
    };
    private static final float[] uv = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            // For text coords in top face
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 1.0f,
            // For text coords in left face
            1.0f, 0.0f,
            1.0f, 1.0f,
            // For text coords in bottom face
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };
}
