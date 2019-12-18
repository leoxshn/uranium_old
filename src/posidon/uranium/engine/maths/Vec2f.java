package posidon.uranium.engine.maths;

import java.util.Objects;

public class Vec2f {
    public float x, y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vec2f sum(Vec2f a, Vec2f b) { return new Vec2f(a.x + b.x, a.y + b.y); }
    public static Vec2f subtract(Vec2f a, Vec2f b) { return new Vec2f(a.x - b.x, a.y - b.y); }
    public static Vec2f multiply(Vec2f a, Vec2f b) { return new Vec2f(a.x * b.x, a.y * b.y); }
    public static Vec2f multiply(Vec2f a, float b) { return new Vec2f(a.x * b, a.y * b); }
    public static Vec2f divide(Vec2f a, Vec2f b) { return new Vec2f(a.x / b.x, a.y / b.y); }
    public static Vec2f divide(Vec2f a, float b) { return new Vec2f(a.x / b, a.y / b); }
    public static float length(Vec2f v) { return (float)Math.sqrt(v.x*v.x + v.y*v.y); }
    public static Vec2f normalize(Vec2f v) { return Vec2f.divide(v, Vec2f.length(v)); }
    public static float dot(Vec2f a, Vec2f b) { return a.x * b.x + a.y * b.y; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec2f)) return false;
        Vec2f v = (Vec2f) o;
        return Float.compare(v.x, x) == 0 && Float.compare(v.y, y) == 0;
    }
    @Override public int hashCode() { return Objects.hash(x, y); }
    @Override public String toString() { return "["+x+", "+y+"]"; }
}
