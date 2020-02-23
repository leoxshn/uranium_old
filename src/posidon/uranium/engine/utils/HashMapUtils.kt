package posidon.uranium.engine.utils

import java.util.*

object HashMapUtils {
    fun newID(map: Map<*, *>): Int {
        var id: Int
        do id = Random().nextInt() while (map.containsKey(id))
        return id
    }
}