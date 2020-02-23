package posidon.uranium.main

import posidon.uranium.engine.maths.Vec3f
import kotlin.math.pow

object Globals {

    var skyColor = Vec3f(0f, 0f, 0f)
    var ambientLight = skyColor
    var time = 0.0
    var timeSpeed = 1

    private val SKY_NORMAL = Vec3f(0.403f, 0.639f, 0.956f)
    private val SKY_NIGHT = Vec3f(0.007f, 0.047f, 0.113f)
    private val LIGHT_DAY = Vec3f(1f, 1f, 1f)
    private val LIGHT_NIGHT = Vec3f(0.098f, 0.137f, 0.180f)
    private const val MAX_TIME = 24000

    fun tick() {
        time = if (time < MAX_TIME) time + timeSpeed else 0.0
        skyColor = Vec3f.blend(SKY_NIGHT, SKY_NORMAL, ((time - MAX_TIME / 2f) / MAX_TIME * 2).pow(2.0).toFloat())
        ambientLight = Vec3f.blend(LIGHT_NIGHT, LIGHT_DAY, ((time - MAX_TIME / 2f) / MAX_TIME * 2).pow(2.0).toFloat())
    }
}