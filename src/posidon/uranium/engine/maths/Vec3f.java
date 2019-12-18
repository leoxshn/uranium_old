package posidon.uranium.engine.maths;

import java.util.Objects;

public class Vec3f {
    public float x, y, z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec3f sum(Vec3f a, Vec3f b) { return new Vec3f(a.x + b.x, a.y + b.y, a.z + b.z); }
    public static Vec3f subtract(Vec3f a, Vec3f b) { return new Vec3f(a.x - b.x, a.y - b.y, a.z - b.z); }
    public static Vec3f multiply(Vec3f a, Vec3f b) { return new Vec3f(a.x * b.x, a.y * b.y, a.z * b.z); }
    public static Vec3f multiply(Vec3f a, float b) { return new Vec3f(a.x * b, a.y * b, a.z * b); }
    public static Vec3f divide(Vec3f a, Vec3f b) { return new Vec3f(a.x / b.x, a.y / b.y, a.z / b.z); }
    public static Vec3f divide(Vec3f a, float b) { return new Vec3f(a.x / b, a.y / b, a.z / b); }
    public static float length(Vec3f v) { return (float)Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z); }
    public static Vec3f normalize(Vec3f v) { return Vec3f.divide(v, Vec3f.length(v)); }
    public static float dot(Vec3f a, Vec3f b) { return a.x * b.x + a.y * b.y + a.z * b.z; }
    public static Vec3f blend(Vec3f v1, Vec3f v2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (v1.x * ratio) + (v2.x * inverseRation);
        float g = (v1.y * ratio) + (v2.y * inverseRation);
        float b = (v1.z * ratio) + (v2.z * inverseRation);
        return new Vec3f(r, g, b);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec3f)) return false;
        Vec3f vec3F = (Vec3f) o;
        return Float.compare(vec3F.x, x) == 0 &&
               Float.compare(vec3F.y, y) == 0 &&
               Float.compare(vec3F.z, z) == 0;
    }
    @Override public int hashCode() { return Objects.hash(x, y, z); }
    @Override public String toString() { return "["+x+", "+y+", "+z+"]"; }

    public Vec3i toVec3i() { return new Vec3i((int)x, (int)y, (int)z); }
}
