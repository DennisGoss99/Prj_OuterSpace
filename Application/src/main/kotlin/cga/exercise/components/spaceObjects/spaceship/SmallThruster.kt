package cga.exercise.components.spaceObjects.spaceship

import cga.exercise.components.geometry.material.AtlasMaterial
import cga.exercise.components.geometry.particle.Particle
import cga.exercise.components.geometry.particle.ParticleSpawner
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.*

class SmallThruster(spaceship: Transformable, position: Vector3f) : IThruster {

    private var thrusterMaterial = AtlasMaterial(5,200, Texture2D("assets/textures/particle/thrusterAtlas.png",true))
    private val thrusterTransformable = Transformable(parent = spaceship)
    private val particleSpawner = ParticleSpawner(Particle(Vector3f(0f), Vector3f(0f,0f,0f),0.7f,-90f,0.7f), thrusterMaterial, thrusterTransformable)

    init {
        thrusterTransformable.translateLocal(position)
    }

    override fun activate(){
        particleSpawner.add()
    }

    override fun render(shaderProgram: ShaderProgram) {
        particleSpawner.render(shaderProgram)
    }

    override fun cleanup() {
        particleSpawner.cleanup()
    }

    override fun bind(shader: ShaderProgram, projectionMatrix: Matrix4f, viewMatrix: Matrix4f){
        particleSpawner.bind(shader, projectionMatrix, viewMatrix)
    }

    override fun update(dt : Float, t: Float) {
        particleSpawner.update(dt)
    }
}