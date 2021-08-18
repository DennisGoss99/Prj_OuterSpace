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

class MainThruster(spaceship: Transformable, position: Vector3f) : IThruster {

    private var thrusterMaterial = AtlasMaterial(5,200, Texture2D("assets/textures/particle/thrusterAtlas.png",true))

    private val thrusterTransformable = Transformable(parent = spaceship)
    private val boostParticleSpawner = ParticleSpawner(Particle(Vector3f(-0.15f,0f,0.1f), Vector3f(0f,0f,0f),0.4f,-90f,2f), thrusterMaterial, thrusterTransformable)
    private val standbyParticleSpawner = ParticleSpawner(Particle(Vector3f(-0.2f,0f,0.1f), Vector3f(0f,0f,0f),1f,-90f,1.75f), thrusterMaterial, thrusterTransformable)


    init {
        thrusterTransformable.translateLocal(position)
    }

    override fun activate(){
        boostParticleSpawner.add()
        clearStandbyParticles()
    }

    fun clearStandbyParticles() = standbyParticleSpawner.update(99f)

    override fun render(shaderProgram: ShaderProgram) {
        boostParticleSpawner.render(shaderProgram)
        standbyParticleSpawner.render(shaderProgram)
    }

    override fun cleanup() {
        boostParticleSpawner.cleanup()
        standbyParticleSpawner.cleanup()
    }

    override fun bind(shader: ShaderProgram, projectionMatrix: Matrix4f, viewMatrix: Matrix4f){
        boostParticleSpawner.bind(shader, projectionMatrix, viewMatrix)
        standbyParticleSpawner.bind(shader, projectionMatrix, viewMatrix)
    }

    private var updateCount = 0f

    override fun update(dt : Float, t: Float) {

        updateCount += dt
        if(updateCount > 0.1f){
            standbyParticleSpawner.add()
            updateCount = 0f
        }

        boostParticleSpawner.update(dt)
        standbyParticleSpawner.update(dt)

    }
}
