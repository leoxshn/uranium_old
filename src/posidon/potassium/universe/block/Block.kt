package posidon.potassium.universe.block

import java.io.Serializable

class Block(val name: String, val hardness: Float, val emission: Float) : Serializable {

    companion object {
        private const val serialVersionUID: Long = 1
    }
}