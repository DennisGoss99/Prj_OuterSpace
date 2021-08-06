package cga.exercise.components.spaceObjects.spaceship

import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.mesh.Renderable
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f

class Spaceship(shouldRender: List<RenderCategory>, renderable: RenderableBase) : Renderable(shouldRender, renderable) {

    init {

    }

    private val thrusters = listOf(
        Thruster(this, Vector3f(1.25f,-0.3f,3.5f)),
        Thruster(this, Vector3f(-1.25f,-0.3f,3.5f))
    )

    fun activateMainThrusters(){
        thrusters[0].activate()
        thrusters[1].activate()
    }

    fun renderThrusters(shaderProgram: ShaderProgram) = thrusters.forEach { it.render(shaderProgram) }

    fun bindThrusters(shader: ShaderProgram, projectionMatrix: Matrix4f, viewMatrix: Matrix4f) = thrusters.forEach { it.bind(shader, projectionMatrix, viewMatrix) }

    fun updateThrusters(dt : Float) = thrusters.forEach{ it.update(dt)}

    override fun cleanup() {
        super.cleanup()
        thrusters.forEach { it.cleanup() }
    }

}