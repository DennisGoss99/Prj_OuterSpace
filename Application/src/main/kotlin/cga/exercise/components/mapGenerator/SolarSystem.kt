package cga.exercise.components.mapGenerator

import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.spaceObjects.AsteroidBelt
import cga.exercise.components.spaceObjects.Moon
import cga.exercise.components.spaceObjects.Planet
import cga.exercise.components.spaceObjects.Sun

class SolarSystem (
    private val suns : List<Sun>,
    private val planets : List<Planet>,
    val asteroidBelts : List<AsteroidBelt>
) : Transformable()
{

    init {
        suns.forEach { it.parent = this }
        planets.forEach { it.parent = this }
        asteroidBelts.forEach { it.parent = this }
    }

    fun renderSpaceObjects(shaderProgram: ShaderProgram){

        suns.forEach { it.render(shaderProgram) }
        planets.forEach { it.render(shaderProgram) }
        asteroidBelts.forEach { it.render(shaderProgram) }


    }

    fun renderAtmosphere(shaderProgram: ShaderProgram){
        suns.forEach { it.atmosphere?.render(shaderProgram) }
        planets.forEach { it.atmosphere?.render(shaderProgram) }
    }

    fun update(dt: Float, t: Float){
        suns.forEach { it.orbit() }
        planets.forEach { it.orbit() }
        asteroidBelts.forEach { it.orbit() }
    }

}