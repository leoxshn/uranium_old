package posidon.uranium.engine.maths

import java.util.*
import kotlin.math.sqrt

class Vec3f(var x: Float, var y: Float, var z: Float) {

    operator fun set(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vec3f) return false
        return other.x.compareTo(x) == 0 && other.y.compareTo(y) == 0 && other.z.compareTo(z) == 0
    }

    override fun hashCode() = Objects.hash(x, y, z)
    override fun toString() = "[$x, $y, $z]"
    fun toVec3i() = Vec3i(x.toInt(), y.toInt(), z.toInt())

    companion object {
        fun sum(a: Vec3f, b: Vec3f) = Vec3f(a.x + b.x, a.y + b.y, a.z + b.z)
        fun subtract(a: Vec3f, b: Vec3f) = Vec3f(a.x - b.x, a.y - b.y, a.z - b.z)
        fun multiply(a: Vec3f, b: Vec3f) = Vec3f(a.x * b.x, a.y * b.y, a.z * b.z)
        fun multiply(a: Vec3f, b: Float) = Vec3f(a.x * b, a.y * b, a.z * b)
        fun divide(a: Vec3f, b: Vec3f) = Vec3f(a.x / b.x, a.y / b.y, a.z / b.z)
        fun divide(a: Vec3f, b: Float) = Vec3f(a.x / b, a.y / b, a.z / b)
        fun length(v: Vec3f) = sqrt(v.x * v.x + v.y * v.y + (v.z * v.z).toDouble()).toFloat()
        fun normalize(v: Vec3f) = divide(v, length(v))
        fun dot(a: Vec3f, b: Vec3f) = a.x * b.x + a.y * b.y + a.z * b.z
        fun blend(v1: Vec3f, v2: Vec3f, ratio: Float): Vec3f {
            val inverseRation = 1f - ratio
            val r = v1.x * ratio + v2.x * inverseRation
            val g = v1.y * ratio + v2.y * inverseRation
            val b = v1.z * ratio + v2.z * inverseRation
            return Vec3f(r, g, b)
        }
    }

}