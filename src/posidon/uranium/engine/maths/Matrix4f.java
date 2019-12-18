package posidon.uranium.engine.maths;

import java.util.Arrays;

public class Matrix4f {
    public static final int SIZE = 4;
    private float[] elements = new float[SIZE * SIZE];

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();
        for (int i = 0; i < SIZE; i++) for (int j = 0; j < SIZE; j++) result.set(i, j, 0);
        result.set(0, 0, 1);
        result.set(1, 1, 1);
        result.set(2, 2, 1);
        result.set(3, 3, 1);
        return result;
    }

    public static Matrix4f translate(Vec3f translation) {
        Matrix4f result = Matrix4f.identity();
        result.set(3, 0, translation.x);
        result.set(3, 1, translation.y);
        result.set(3, 2, translation.z);
        return result;
    }

    public static Matrix4f translate(Vec2f translation) {
        Matrix4f result = Matrix4f.identity();
        result.set(3, 0, translation.x);
        result.set(3, 1, translation.y);
        result.set(3, 2, 0);
        return result;
    }

    public static Matrix4f rotate(float angle, Vec3f axis) {
        Matrix4f result = Matrix4f.identity();

        float cos = (float) Math.cos(Math.toRadians(angle));
        float sin = (float) Math.sin(Math.toRadians(angle));
        float C = 1 - cos;

        result.set(0, 0, cos + axis.x * axis.x * C);
        result.set(0, 1, axis.x * axis.y * C - axis.z * sin);
        result.set(0, 2, axis.x * axis.z * C + axis.y * sin);
        result.set(1, 0, axis.y * axis.x * C + axis.z * sin);
        result.set(1, 1, cos + axis.y * axis.y * C);
        result.set(1, 2, axis.y * axis.z * C - axis.x * sin);
        result.set(2, 0, axis.z * axis.x * C - axis.y * sin);
        result.set(2, 1, axis.z * axis.y * C + axis.x * sin);
        result.set(2, 2, cos + axis.z * axis.z * C);

        return result;
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

    public static Matrix4f scale(Vec3f scale) {
        Matrix4f result = Matrix4f.identity();
        result.set(0, 0, scale.x);
        result.set(1, 1, scale.y);
        result.set(2, 2, scale.z);
        return result;
    }

    public static Matrix4f multiply(Matrix4f a, Matrix4f b) {
        Matrix4f result = Matrix4f.identity();
        for (int i = 0; i < SIZE; i++) for (int j = 0; j < SIZE; j++) {
            result.set(i, j, a.get(i, 0) * b.get(0, j) +
                            a.get(i, 1) * b.get(1, j) +
                            a.get(i, 2) * b.get(2, j) +
                            a.get(i, 3) * b.get(3, j));
        }
        return result;
    }

    public static Matrix4f transform(Vec3f position, Vec3f rotation, Vec3f scale) {
        Matrix4f translation = Matrix4f.translate(position);
        Matrix4f rotX = Matrix4f.rotate(rotation.x, new Vec3f(1, 0, 0));
        Matrix4f rotY = Matrix4f.rotate(rotation.y, new Vec3f(0, 1, 0));
        Matrix4f rotZ = Matrix4f.rotate(rotation.z, new Vec3f(0, 0, 1));
        Matrix4f scaleMatrix = Matrix4f.scale(scale);
        Matrix4f rotationMatrix = Matrix4f.multiply(rotX, Matrix4f.multiply(rotY, rotZ));
        return Matrix4f.multiply(Matrix4f.multiply(rotationMatrix, scaleMatrix), translation);
        /*Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;*/
    }

    public static Matrix4f transform(Vec2f position, Vec2f size) {
        Matrix4f translation = Matrix4f.translate(position);
        Matrix4f scaleMatrix = Matrix4f.identity();
        scaleMatrix.set(0, 0, size.x);
        scaleMatrix.set(1, 1, size.y);
        return Matrix4f.multiply(scaleMatrix, translation);
        /*Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;*/
    }

    public static Matrix4f projection(float fov, float aspectRatio, float near, float far) {
        Matrix4f result = Matrix4f.identity();
        float tanfov = (float) Math.tan(Math.toRadians(fov / 2));
        float range = far - near;
        result.set(0, 0, 1f / (aspectRatio * tanfov));
        result.set(1, 1, 1f / tanfov);
        result.set(2, 2, -(far + near)/range);
        result.set(2, 3, -1);
        result.set(3, 2, -(2 * far * near)/range);
        result.set(3, 3, 0);
        return result;
    }

    public static Matrix4f view(Vec3f position, Vec2f rotation) {
        Matrix4f translation = Matrix4f.translate(new Vec3f(-position.x, -position.y, -position.z));
        Matrix4f rotX = Matrix4f.rotate(rotation.x, new Vec3f(1, 0, 0));
        Matrix4f rotY = Matrix4f.rotate(rotation.y, new Vec3f(0, 1, 0));
        Matrix4f rotationMatrix = Matrix4f.multiply(rotY, rotX);
        return Matrix4f.multiply(translation, rotationMatrix);
    }

    public float get(int x, int y) { return elements[y * SIZE + x]; }
    public void set(int x, int y, float value) { elements[y * SIZE + x] = value; }
    public float[] getAll() { return elements; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix4f)) return false;
        Matrix4f matrix4f = (Matrix4f) o;
        return Arrays.equals(elements, matrix4f.elements);
    }

    @Override
    public int hashCode() { return Arrays.hashCode(elements); }
}
