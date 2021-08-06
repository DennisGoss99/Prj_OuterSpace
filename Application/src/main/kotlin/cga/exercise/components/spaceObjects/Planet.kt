package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.atmosphere.Atmosphere
import cga.exercise.components.geometry.material.IMaterial
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

class Planet(val name: String,
             size: Float,
             distanceToParent : Float,
             speed : Float,
             rotationAngle : Float,
             selfRotation : Vector3f,
             material : IMaterial,
             val atmosphere: Atmosphere? = null,
             private val ring : PlanetRing? = null,
             private val moons: List<Moon> = listOf(),
             renderable : RenderableBase
) : SpaceObject(size, distanceToParent, speed, rotationAngle, selfRotation, material, null, renderable){

    init {

        atmosphere?.parent = this
        moons.forEach { it.parent = this }
        ring?.parent = this
    }

    override fun render(shaderProgram: ShaderProgram) {
        super.render(shaderProgram)
        moons.forEach { it.render(shaderProgram) }
        ring?.render(shaderProgram)
    }

    override fun orbit() {
        super.orbit()
        moons.forEach { it.orbit() }
    }

}