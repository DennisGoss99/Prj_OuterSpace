package cga.exercise.components.geometry

import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f


class Renderable (var meshes: MutableList<Mesh>, modelMatrix: Matrix4f = Matrix4f(), parent: Transformable? = null) : Transformable(modelMatrix, parent),IRenderable {

    override fun render(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        shaderProgram.setUniform("model_matrix" , getWorldModelMatrix(),false)
        meshes.forEach { m -> m.render(shaderProgram) }
    }
}