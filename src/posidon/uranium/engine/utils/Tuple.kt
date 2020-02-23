package posidon.uranium.engine.utils

class Tuple<A, B>(private var a: A, private var b: B) {
    fun set(tuple: Tuple<A, B>) {
        a = tuple.a
        b = tuple.b
    }

    fun get0(): A {
        return a
    }

    fun get1(): B {
        return b
    }

    override fun toString(): String {
        return "($a, $b)"
    }

}