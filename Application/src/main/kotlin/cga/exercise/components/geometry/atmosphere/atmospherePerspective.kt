package cga.exercise.components.geometry.atmosphere

import cga.exercise.components.camera.IPerspective
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector4f

class atmospherePerspective {

    companion object : IPerspective{
        override fun bind(shader: ShaderProgram, projectionMatrix: Matrix4f, viewMatrix: Matrix4f) {
            shader.use()

            shader.setUniform("view_matrix", viewMatrix,false)
            shader.setUniform("projection_matrix", projectionMatrix,false)
        }
    }

}