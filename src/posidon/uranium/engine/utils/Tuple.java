package posidon.uranium.engine.utils;

public class Tuple <A, B> {

    private A a;
    private B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public void set(Tuple<A, B> tuple) {
        this.a = tuple.a;
        this.b = tuple.b;
    }

    public A get0() { return a; }
    public B get1() { return b; }

    @Override
    public String toString() {
        return "("+a+", "+b+")";
    }
}
