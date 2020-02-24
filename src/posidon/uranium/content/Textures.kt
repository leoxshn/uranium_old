package posidon.uranium.content

import posidon.uranium.engine.graphics.Texture
import java.util.*

object Textures {

    var blocks = HashMap<String, Texture>()
    private val blockNames = arrayOf("grass", "stone")

    fun set(path: String?) {
        if (path == null) for (name in blockNames) blocks[name] = Texture("res/textures/block/$name.png")
    }

    fun clear() {
        for (texture in blocks.values) texture.delete()
        blocks.clear()
    }
}