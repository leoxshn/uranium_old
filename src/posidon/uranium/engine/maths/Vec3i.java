package posidon.uranium.engine.maths;

import java.util.Objects;

public class Vec3i {
    public int x, y, z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec3i sum(Vec3i a, Vec3i b) { return new Vec3i(a.x + b.x, a.y + b.y, a.z + b.z); }
    public static Vec3i subtract(Vec3i a, Vec3i b) { return new Vec3i(a.x - b.x, a.y - b.y, a.z - b.z); }
    public static Vec3i multiply(Vec3i a, Vec3i b) { return new Vec3i(a.x * b.x, a.y * b.y, a.z * b.z); }
    public static Vec3i multiply(Vec3i a, int b) { return new Vec3i(a.x * b, a.y * b, a.z * b); }
    public static Vec3i divide(Vec3i a, Vec3i b) { return new Vec3i(a.x / b.x, a.y / b.y, a.z / b.z); }
    public static Vec3i divide(Vec3i a, int b) { return new Vec3i(a.x / b, a.y / b, a.z / b); }
    public static Vec3i modulus(Vec3i a, int b) { return new Vec3i(Math.abs(a.x % b), Math.abs(a.y % b), Math.abs(a.z % b)); }
    public static Vec3i remainder(Vec3i a, int b) { return new Vec3i(a.x % b, a.y % b, a.z % b); }
    public static float length(Vec3i v) { return (float) Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z); }
    public static Vec3i normalize(Vec3i v) { return Vec3i.divide(v, (int) Vec3i.length(v)); }
    public static int dot(Vec3i a, Vec3i b) { return a.x * b.x + a.y * b.y + a.z * b.z; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec3i)) return false;
        Vec3i vec3F = (Vec3i) o;
        return vec3F.x == x && vec3F.y == y && vec3F.z == z;
    }
    @Override public int hashCode() { return Objects.hash(x, y, z); }
    @Override public String toString() { return "["+x+", "+y+", "+z+"]"; }

    public Vec3f toVec3f() { return new Vec3f(x, y, z); }
}
