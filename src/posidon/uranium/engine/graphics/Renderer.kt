package posidon.uranium.engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import posidon.potassium.universe.block.Block
import posidon.uranium.content.Textures
import posidon.uranium.engine.Window
import posidon.uranium.engine.maths.Matrix4f
import posidon.uranium.engine.maths.Vec3i
import posidon.uranium.engine.objects.Camera
import posidon.uranium.engine.objects.Chunk
import posidon.uranium.engine.objects.Cube
import posidon.uranium.engine.objects.GameObject
import posidon.uranium.engine.ui.View
import posidon.uranium.engine.utils.Tuple
import posidon.uranium.main.Globals
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

object Renderer {

    private var mainShader: Shader? = null
    private var blockShader: Shader? = null
    private var uiShader: Shader? = null
    private val objects = ConcurrentHashMap<Model?, MutableList<GameObject>>()
    val ui = ArrayList<View?>()
    val chunks = ConcurrentHashMap<Vec3i?, Chunk>()
    private val blockQueue = ConcurrentLinkedQueue<Tuple<Tuple<Vec3i, Vec3i>, Block?>>()
    var viewMatrix: Matrix4f = Matrix4f.view(Camera.position, Camera.rotation)

    fun init() {
        mainShader = Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl")
        mainShader!!.create()
        blockShader = Shader("/shaders/blockVertex.glsl", "/shaders/blockFragment.glsl")
        blockShader!!.create()
        uiShader = Shader("/shaders/viewVertex.glsl", "/shaders/viewFragment.glsl")
        uiShader!!.create()
        View.init()
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_BACK)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    }

    fun render() { /*mainShader.bind();
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
        blockShader!!.bind()
        blockShader!!.setUniform("skyColor", Globals.skyColor)
        blockShader!!.setUniform("ambientLight", Globals.ambientLight)
        blockShader!!.setUniform("projection", Window.projectionMatrix)
        blockShader!!.setUniform("view", viewMatrix)
        for (chunkPos in chunks.keys) if (chunks[chunkPos]!!.isInFOV) {
            val chunk = chunks[chunkPos]
            for (sides in chunk!!.cubesBySides.keys) {
                GL30.glBindVertexArray(Cube.meshes[sides]!!.vaoId)
                GL30.glEnableVertexAttribArray(0)
                GL30.glEnableVertexAttribArray(1)
                GL13.glActiveTexture(GL13.GL_TEXTURE0)
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Cube.meshes[sides]!!.getVbo(2))
                for (cube in chunk.cubesBySides[sides]!!) {
                    cube.texture!!.bind()
                    blockShader!!.setUniform("emission", cube.emission)
                    blockShader!!.setUniform("model", Matrix4f.translate(cube.absolutePosition.toVec3f()))
                    GL11.glDrawElements(GL11.GL_TRIANGLES, cube.mesh!!.vertexCount, GL11.GL_UNSIGNED_INT, 0)
                }
            }
        }
        uiShader!!.bind()
        GL30.glBindVertexArray(View.MESH.vaoId)
        GL30.glEnableVertexAttribArray(0)
        GL30.glEnableVertexAttribArray(1)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, View.MESH.getVbo(2))
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        for (view in ui) if (view!!.visible) view.render(uiShader)
    }

    fun updateBlocks() {
        for (i in blockQueue.indices) {
            val tuple = blockQueue.poll()
            actuallySetBlock(tuple.b, tuple.a)
        }
    }

    fun bg() {
        chunks.keys.removeIf { chunkPos: Vec3i? ->
            if ((chunkPos!! * Chunk.CHUNK_SIZE - Camera.position!!.toVec3i()).length() > 200) {
                chunks[chunkPos]!!.clear()
                true
            } else false
        }
    }

    operator fun set(posInChunk: Vec3i, chunkPos: Vec3i, block: Block?) {
        blockQueue.add(Tuple(Tuple(posInChunk, chunkPos), block))
    }

    private fun actuallySetBlock(block: Block?, positions: Tuple<Vec3i, Vec3i>) {
        if (chunks[positions.b] == null) chunks[positions.b] = Chunk(positions.b)
        val cube = chunks[positions.b]!![positions.a]
        cube?.chunk?.cubesBySides?.get(cube.sides)?.remove(cube)
        if (block == null) chunks[positions.b]!![positions.a] = null else chunks[positions.b]!![positions.a] = Cube(block, positions.a, positions.b)
    }

    fun add(obj: GameObject) {
        val model = obj.model
        val batch = objects[model]
        if (batch == null) {
            val newBatch: MutableList<GameObject> = ArrayList()
            newBatch.add(obj)
            objects[model] = newBatch
        } else batch.add(obj)
    }

    fun kill() {
        mainShader!!.kill()
        blockShader!!.kill()
        objects.clear()
        for (chunk in chunks.values) chunk.clear()
        chunks.clear()
        blockQueue.clear()
        ui.clear()
        Cube.kill()
        Textures.clear()
    }
}