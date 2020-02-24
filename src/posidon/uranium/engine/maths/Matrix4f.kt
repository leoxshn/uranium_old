package posidon.uranium.engine.maths

import java.util.*
import kotlin.math.tan

class Matrix4f {

    val all = FloatArray(SIZE * SIZE)

    operator fun get(x: Int, y: Int) = all[y * SIZE + x]
    operator fun set(x: Int, y: Int, value: Float) { all[y * SIZE + x] = value }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix4f) return false
        return all.contentEquals(other.all)
    }

    override fun hashCode(): Int = all.contentHashCode()

    companion object {

        const val SIZE = 4

        fun identity(): Matrix4f {
            val result = Matrix4f()
            for (i in 0 until SIZE) for (j in 0 until SIZE) result[i, j] = 0f
            result[0, 0] = 1f
            result[1, 1] = 1f
            result[2, 2] = 1f
            result[3, 3] = 1f
            return result
        }

        fun translate(translation: Vec3f?): Matrix4f {
            val result = identity()
            result[3, 0] = translation!!.x
            result[3, 1] = translation.y
            result[3, 2] = translation.z
            return result
        }

        fun translate(translation: Vec2f): Matrix4f {
            val result = identity()
            result[3, 0] = translation.x
            result[3, 1] = translation.y
            result[3, 2] = 0f
            return result
        }

        fun rotate(angle: Float, axis: Vec3f): Matrix4f {
            val result = identity()
            val cos = Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val sin = Math.sin(Math.toRadians(angle.toDouble())).toFloat()
            val C = 1 - cos
            result[0, 0] = cos + axis.x * axis.x * C
            result[0, 1] = axis.x * axis.y * C - axis.z * sin
            result[0, 2] = axis.x * axis.z * C + axis.y * sin
            result[1, 0] = axis.y * axis.x * C + axis.z * sin
            result[1, 1] = cos + axis.y * axis.y * C
            result[1, 2] = axis.y * axis.z * C - axis.x * sin
            result[2, 0] = axis.z * axis.x * C - axis.y * sin
            result[2, 1] = axis.z * axis.y * C + axis.x * sin
            result[2, 2] = cos + axis.z * axis.z * C
            return result
        }

        /*

        result.set(0, 0, 1);
        result.set(0, 1, 0);
        result.set(0, 2, 0);
        result.set(1, 0, 0);
        result.set(1, 1, 1);
        result.set(1, 2, 0);
        result.set(2, 0, 0);
        result.set(2, 1, 0);
        result.set(2, 2, 1);*/
        fun scale(scale: Vec3f): Matrix4f {
            val result = identity()
            result[0, 0] = scale.x
            result[1, 1] = scale.y
            result[2, 2] = scale.z
            return result
        }

        fun multiply(a: Matrix4f, b: Matrix4f): Matrix4f {
            val result = identity()
            for (i in 0 until SIZE) for (j in 0 until SIZE)
                result[i, j] = a[i, 0] * b[0, j] + a[i, 1] * b[1, j] + a[i, 2] * b[2, j] + a[i, 3] * b[3, j]
            return result
        }

        fun transform(position: Vec3f?, rotation: Vec3f, scale: Vec3f): Matrix4f {
            val translation = translate(position)
            val rotX = rotate(rotation.x, Vec3f(1f, 0f, 0f))
            val rotY = rotate(rotation.y, Vec3f(0f, 1f, 0f))
            val rotZ = rotate(rotation.z, Vec3f(0f, 0f, 1f))
            val scaleMatrix = scale(scale)
            val rotationMatrix = multiply(rotX, multiply(rotY, rotZ))
            return multiply(multiply(rotationMatrix, scaleMatrix), translation)
            /*Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;*/
        }

        fun transform(position: Vec2f, size: Vec2f): Matrix4f {
            val translation = translate(position)
            val scaleMatrix = identity()
            scaleMatrix[0, 0] = size.x
            scaleMatrix[1, 1] = size.y
            return multiply(scaleMatrix, translation)
            /*Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;*/
        }

        fun projection(fov: Float, aspectRatio: Float, near: Float, far: Float): Matrix4f {
            val result = identity()
            val tanfov = tan(Math.toRadians(fov / 2.toDouble())).toFloat()
            val range = far - near
            result[0, 0] = 1f / (aspectRatio * tanfov)
            result[1, 1] = 1f / tanfov
            result[2, 2] = -(far + near) / range
            result[2, 3] = -1f
            result[3, 2] = -(2 * far * near) / range
            result[3, 3] = 0f
            return result
        }

        fun view(position: Vec3f?, rotation: Vec2f?): Matrix4f {
            val translation = translate(Vec3f(-position!!.x, -position.y, -position.z))
            val rotX = rotate(rotation!!.x, Vec3f(1f, 0f, 0f))
            val rotY = rotate(rotation.y, Vec3f(0f, 1f, 0f))
            val rotationMatrix = multiply(rotY, rotX)
            return multiply(translation, rotationMatrix)
        }
    }
}