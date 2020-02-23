package posidon.potassium.universe.block

import java.io.Serializable

class Block : Serializable {
    var name: String? = null
    var hardness = 0f
    var emission = 0f

    companion object {
        private const val serialVersionUID: Long = 1
    }
}