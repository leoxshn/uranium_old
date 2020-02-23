package posidon.uranium.engine.maths

import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt

class Vec3i(var x: Int, var y: Int, var z: Int) {

    operator fun set(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vec3i) return false
        return other.x == x && other.y == y && other.z == z
    }

    override fun hashCode() = Objects.hash(x, y, z)
    override fun toString() = "[$x, $y, $z]"
    fun toVec3f() = Vec3f(x.toFloat(), y.toFloat(), z.toFloat())

    companion object {
        fun sum(a: Vec3i, b: Vec3i?) = Vec3i(a.x + b!!.x, a.y + b.y, a.z + b.z)
        fun subtract(a: Vec3i, b: Vec3i) = Vec3i(a.x - b.x, a.y - b.y, a.z - b.z)
        fun multiply(a: Vec3i, b: Vec3i) = Vec3i(a.x * b.x, a.y * b.y, a.z * b.z)
        fun multiply(a: Vec3i?, b: Int) = Vec3i(a!!.x * b, a.y * b, a.z * b)
        fun divide(a: Vec3i, b: Vec3i) = Vec3i(a.x / b.x, a.y / b.y, a.z / b.z)
        fun divide(a: Vec3i, b: Int) = Vec3i(a.x / b, a.y / b, a.z / b)
        fun modulus(a: Vec3i, b: Int) = Vec3i(abs(a.x % b), abs(a.y % b), abs(a.z % b))
        fun remainder(a: Vec3i, b: Int) = Vec3i(a.x % b, a.y % b, a.z % b)
        fun length(v: Vec3i) = sqrt(v.x * v.x + v.y * v.y + (v.z * v.z).toDouble()).toFloat()
        fun normalize(v: Vec3i) = divide(v, length(v).toInt())
        fun dot(a: Vec3i, b: Vec3i) = a.x * b.x + a.y * b.y + a.z * b.z
    }

}