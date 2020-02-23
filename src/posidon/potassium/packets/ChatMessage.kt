package posidon.potassium.packets

import java.io.Serializable

class ChatMessage(var message: String) : Serializable {

    companion object {
        private const val serialVersionUID: Long = 1
    }

}