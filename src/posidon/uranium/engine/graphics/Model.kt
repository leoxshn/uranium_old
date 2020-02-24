package posidon.uranium.engine.graphics

class Model {

    val mesh: Mesh
    val texture: Texture

    constructor(mesh: Mesh, texture: Texture) {
        this.mesh = mesh
        this.texture = texture
    }

    constructor(positions: FloatArray, indices: IntArray, uv: FloatArray, texture: Texture) {
        mesh = Mesh(positions, indices, uv)
        this.texture = texture
    }

    fun delete() {
        mesh.delete()
        texture.delete()
    }
}