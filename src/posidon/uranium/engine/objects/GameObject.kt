package posidon.uranium.engine.objects

import posidon.uranium.engine.graphics.Model
import posidon.uranium.engine.maths.Vec3f

class GameObject(val model: Model, var position: Vec3f, var rotation: Vec3f, var scale: Vec3f) {
    fun tick() {}
    fun kill() { model.delete() }
}