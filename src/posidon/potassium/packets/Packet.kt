package posidon.potassium.packets

import java.io.Serializable
import java.util.*

class Packet : HashMap<String?, Any?>(), Serializable {

    fun getInt(key: String?) = super.get(key) as Int
    fun getDouble(key: String?) = super.get(key) as Double
    fun getFloat(key: String?) = super.get(key) as Float
    fun getString(key: String?) =
            try { super.get(key) as String? }
            catch (e: Exception) { null }

    companion object {
        private const val serialVersionUID: Long = 1
    }
}