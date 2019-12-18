package posidon.uranium.engine.objects;

import posidon.uranium.engine.graphics.Model;
import posidon.uranium.engine.maths.Vec3f;

public class GameObject {
    public Vec3f position, rotation, scale;
    public final Model model;

    public GameObject(Model model, Vec3f position, Vec3f rotation, Vec3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.model = model;
    }

    public void tick() {}

    public void kill() { model.delete(); }
}
