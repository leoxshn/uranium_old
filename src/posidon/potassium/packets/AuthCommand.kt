package posidon.potassium.packets

import java.io.Serializable

class AuthCommand(var key: String, var cmd: Array<String>) : Serializable {

    companion object {
        private const val serialVersionUID: Long = 1
    }

}