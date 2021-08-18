package cga.exercise.components.spaceObjects.spaceship

import cga.exercise.components.geometry.mesh.Renderable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f

class Spaceship(renderable: Renderable) : Renderable(renderable.shouldRender, renderable) {

    init {

        }

    private val mainThruster = listOf(
        MainThruster(this, Vector3f(1.25f,-0.3f,3.5f)),
        MainThruster(this, Vector3f(-1.25f,-0.3f,3.5f))
    )

    private val thrusters = listOf(
        mainThruster[0],
        mainThruster[1],
        SmallThruster(this, Vector3f(7.2f,1f,1.9f)),
        SmallThruster(this, Vector3f(-7.2f,1f,1.9f))
    )

    fun activateMainThrusters(){
        thrusters[0].activate()
        thrusters[1].activate()

    }

    fun activateRightTurnThruster() {
        thrusters[2].activate()
    }

    fun activateLeftTurnThruster(){
        thrusters[3].activate()
    }

    fun renderThrusters(shaderProgram: ShaderProgram) = thrusters.forEach { it.render(shaderProgram) }

    fun bindThrusters(shader: ShaderProgram, projectionMatrix: Matrix4f, viewMatrix: Matrix4f) = thrusters.forEach { it.bind(shader, projectionMatrix, viewMatrix) }

    fun updateThrusters(dt : Float, t: Float) {
        thrusters.forEach{ it.update(dt, t)}
    }

    override fun cleanup() {
        super.cleanup()
        thrusters.forEach { it.cleanup() }
    }

}