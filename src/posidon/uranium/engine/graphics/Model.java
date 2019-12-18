package posidon.uranium.engine.graphics;

public class Model {

    public final Mesh mesh;
    public final Texture texture;

    public Model(Mesh mesh, Texture texture) {
        this.mesh = mesh;
        this.texture = texture;
    }

    public Model(float[] positions, int[] indices, float[] uv, Texture texture) {
        this.mesh = new Mesh(positions, indices, uv);
        this.texture = texture;
    }

    public void delete() {
        mesh.delete();
        texture.delete();
    }
}
