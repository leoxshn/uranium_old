package posidon.potassium.backend

import java.io.Serializable

class PlayerInfo : Serializable {
    var x = 0f
    var y = 0f
    var z = 0f

    companion object {
        private const val serialVersionUID: Long = 1
    }
}