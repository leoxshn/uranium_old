package posidon.uranium.engine.graphics;

import posidon.potassium.universe.block.Block;
import posidon.uranium.content.Textures;
import posidon.uranium.engine.Window;
import posidon.uranium.engine.maths.Matrix4f;
import posidon.uranium.engine.maths.Vec3i;
import posidon.uranium.engine.objects.Camera;
import posidon.uranium.engine.objects.Chunk;
import posidon.uranium.engine.objects.Cube;
import posidon.uranium.engine.objects.GameObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import posidon.uranium.engine.utils.Tuple;
import posidon.uranium.engine.ui.View;
import posidon.uranium.main.Globals;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Renderer {
    private static Shader mainShader;
    private static Shader blockShader;
    private static Shader uiShader;
    private static final ConcurrentHashMap<Model, List<GameObject>> objects = new ConcurrentHashMap<>();
    public static final ArrayList<View> ui = new ArrayList<>();
    public static final ConcurrentHashMap<Vec3i, Chunk> chunks = new ConcurrentHashMap<>();
    private static ConcurrentLinkedQueue<Tuple<Tuple<Vec3i, Vec3i>, Block>> blockQueue = new ConcurrentLinkedQueue<>();
    public static Matrix4f viewMatrix = Matrix4f.view(Camera.position, Camera.rotation);

    public static void init() {
        mainShader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
        mainShader.create();
        blockShader = new Shader("/shaders/blockVertex.glsl", "/shaders/blockFragment.glsl");
        blockShader.create();
        uiShader = new Shader("/shaders/viewVertex.glsl", "/shaders/viewFragment.glsl");
        uiShader.create();
        View.init();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void render() {
        /*mainShader.bind();
        mainShader.setUniform("skyColor", Globals.skyColor);
        mainShader.setUniform("ambientLight", Globals.ambientLight);
        mainShader.setUniform("projection", Window.getProjectionMatrix());
        mainShader.setUniform("view", viewMatrix);
        for (Model model : objects.keySet()) {
            List<GameObject> batch = objects.get(model);
            GL30.glBindVertexArray(model.mesh.vaoId);
            GL30.glEnableVertexAttribArray(0);
            GL30.glEnableVertexAttribArray(1);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.mesh.getVbo(2));
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            model.texture.bind();
            for (GameObject obj : batch) {
                mainShader.setUniform("model", Matrix4f.transform(obj.position, obj.rotation, obj.scale));
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.mesh.vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            GL30.glDisableVertexAttribArray(0);
            GL30.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);
        }*/

        blockShader.bind();
        blockShader.setUniform("skyColor", Globals.skyColor);
        blockShader.setUniform("ambientLight", Globals.ambientLight);
        blockShader.setUniform("projection", Window.getProjectionMatrix());
        blockShader.setUniform("view", viewMatrix);
        for (Vec3i chunkPos : chunks.keySet()) if (chunks.get(chunkPos).isInFOV()) {
            Chunk chunk = chunks.get(chunkPos);
            for (boolean[] sides : chunk.cubesBySides.keySet()) {
                GL30.glBindVertexArray(Cube.meshes.get(sides).vaoId);
                GL30.glEnableVertexAttribArray(0);
                GL30.glEnableVertexAttribArray(1);
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Cube.meshes.get(sides).getVbo(2));
                for (Cube cube : chunk.cubesBySides.get(sides)) {
                    cube.getTexture().bind();
                    blockShader.setUniform("emission", cube.emission);
                    blockShader.setUniform("model", Matrix4f.translate(cube.absolutePosition.toVec3f()));
                    GL11.glDrawElements(GL11.GL_TRIANGLES, cube.getMesh().vertexCount, GL11.GL_UNSIGNED_INT, 0);
                }
            }
        }

        uiShader.bind();
        GL30.glBindVertexArray(View.getMESH().vaoId);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, View.getMESH().getVbo(2));
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        for (View view : ui)
            if (view.visible)
                view.render(uiShader);
    }

    public static void updateBlocks() {
        for (int i = 0; i < blockQueue.size(); i++) {
            Tuple<Tuple<Vec3i, Vec3i>, Block> tuple = blockQueue.poll();
            actuallySetBlock(tuple.get1(), tuple.get0());
        }
    }

    public static void bg() {
        chunks.keySet().removeIf(chunkPos -> {
            if (Vec3i.length(Vec3i.subtract(Vec3i.multiply(chunkPos, Chunk.CHUNK_SIZE), Camera.position.toVec3i())) > 200) {
                chunks.get(chunkPos).clear();
                return true;
            }
            return false;
        });
    }

    public static void setBlock(Block block, Vec3i posInChunk, Vec3i chunkPos) {
        blockQueue.add(new Tuple<>(new Tuple<>(posInChunk, chunkPos), block));
    }

    private static void actuallySetBlock(Block block, Tuple<Vec3i, Vec3i> positions) {
        if (chunks.get(positions.get1()) == null) chunks.put(positions.get1(), new Chunk(positions.get1()));
        Cube cube = chunks.get(positions.get1()).getCube(positions.get0());
        if (cube != null) cube.getChunk().cubesBySides.get(cube.getSides()).remove(cube);
        if (block == null) chunks.get(positions.get1()).setCube(null, positions.get0());
        else chunks.get(positions.get1()).setCube(new Cube(block, positions.get0(), positions.get1()), positions.get0());
    }

    public static void add(GameObject obj) {
        Model model = obj.model;
        List<GameObject> batch = objects.get(model);
        if (batch == null) {
            List<GameObject> newBatch = new ArrayList<>();
            newBatch.add(obj);
            objects.put(model, newBatch);
        } else { batch.add(obj); }
    }

    public static void kill() {
        mainShader.kill();
        blockShader.kill();
        objects.clear();
        for (Chunk chunk : chunks.values()) chunk.clear();
        chunks.clear();
        blockQueue.clear();
        ui.clear();
        Cube.kill();
        Textures.clear();
    }
}
