package posidon.uranium.engine.maths

import java.util.*
import kotlin.math.sqrt

class Vec2f(var x: Float, var y: Float) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vec2f) return false
        return other.x.compareTo(x) == 0 && other.y.compareTo(y) == 0
    }

    override fun hashCode() = Objects.hash(x, y)
    override fun toString() = "[$x, $y]"

    operator fun plus(other: Vec2f) = Vec2f(x + other.x, y + other.y)
    operator fun minus(other: Vec2f) = Vec2f(x - other.x, y - other.y)
    operator fun times(other: Vec2f) = Vec2f(x * other.x, y * other.y)
    operator fun times(other: Float) = Vec2f(x * other, y * other)
    operator fun div(other: Vec2f) = Vec2f(x / other.x, y / other.y)
    operator fun div(float: Float) = Vec2f(x / float, y / float)
    fun length() = sqrt(x * x + y * y)
    fun normalize() = this / length()
    fun dot(other: Vec2f) = x * other.x + y * other.y
}