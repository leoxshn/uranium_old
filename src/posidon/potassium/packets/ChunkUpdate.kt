package posidon.potassium.packets

import posidon.potassium.universe.block.Block
import java.io.Serializable

class ChunkUpdate : Serializable {
    var x = 0
    var y = 0
    var z = 0
    lateinit var blocks: Array<Block>

    companion object {
        private const val serialVersionUID: Long = 1
    }
}