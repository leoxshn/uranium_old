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

    companion object {
        fun sum(a: Vec2f?, b: Vec2f) = Vec2f(a!!.x + b.x, a.y + b.y)
        fun subtract(a: Vec2f, b: Vec2f) = Vec2f(a.x - b.x, a.y - b.y)
        fun multiply(a: Vec2f, b: Vec2f) = Vec2f(a.x * b.x, a.y * b.y)
        fun multiply(a: Vec2f, b: Float) = Vec2f(a.x * b, a.y * b)
        fun divide(a: Vec2f, b: Vec2f) = Vec2f(a.x / b.x, a.y / b.y)
        fun divide(a: Vec2f, b: Float) = Vec2f(a.x / b, a.y / b)
        fun length(v: Vec2f) = sqrt(v.x * v.x + v.y * v.y)
        fun normalize(v: Vec2f) = divide(v, length(v))
        fun dot(a: Vec2f, b: Vec2f) = a.x * b.x + a.y * b.y
    }

}