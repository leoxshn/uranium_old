package posidon.potassium.packets

import java.io.Serializable

class PlayerJoinPacket(var id: Int, var name: String) : Serializable {
    var player: Any? = null

    companion object {
        private const val serialVersionUID: Long = 1
    }

}