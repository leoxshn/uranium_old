package posidon.potassium.packets

import java.io.Serializable

class InitInfoPacket : Serializable {
    var time = 0.0
    var x = 0f
    var y = 0f
    var z = 0f
    var moveSpeed = 0f
    var jumpHeight = 0f

    companion object {
        private const val serialVersionUID: Long = 1
    }
}