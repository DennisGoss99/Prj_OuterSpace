package cga.exercise.components.spaceObjects.spaceship

import cga.exercise.components.camera.IPerspective
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.material.AtlasMaterial
import cga.exercise.components.geometry.particle.Particle
import cga.exercise.components.geometry.particle.ParticleSpawner
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Matrix4f
import org.joml.Vector3f

class Thruster(spaceship: Transformable, position: Vector3f) : IRenderable, IPerspective {

    private var thrusterMaterial = AtlasMaterial(5,200, Texture2D("assets/textures/particle/thrusterAtlas.png",true))

    private val thrusterTransformable = Transformable(parent = spaceship)
    private val particleSpawner = ParticleSpawner(Particle(Vector3f(0f), Vector3f(0f,0f,0f),1f,-90f,2f), thrusterMaterial, thrusterTransformable)


    init {

        thrusterTransformable.translateLocal(position)
    }

    fun activate(){
        particleSpawner.add()
    }

    override fun render(shaderProgram: ShaderProgram) {
        particleSpawner.render(shaderProgram)
    }

    override fun cleanup() {
        particleSpawner.cleanup()
    }

    override fun bind(shader: ShaderProgram, projectionMatrix: Matrix4f, viewMatrix: Matrix4f) = particleSpawner.bind(shader, projectionMatrix, viewMatrix)

    fun update(dt : Float) = particleSpawner.update(dt)
}
