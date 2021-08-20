package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

class AsteroidBelt(asteroidCount: Int, distanceNear : Int , distanceFar : Int, modelMatrix: Matrix4f = Matrix4f(), parent: Transformable? = null) : Transformable(modelMatrix, parent) {

    private val asteroids = mutableListOf<Asteroid>()

    init {

        for (i in 0 .. asteroidCount){
            asteroids.add(Asteroid.getRandomAsteroid( distanceNear, distanceFar))
        }

        asteroids.forEach { it.parent = this }
    }

    fun render(shaderProgram: ShaderProgram) {
        asteroids.forEach { it.render(shaderProgram) }
    }

    fun orbit() {
        asteroids.forEach { it.orbit() }
    }

}