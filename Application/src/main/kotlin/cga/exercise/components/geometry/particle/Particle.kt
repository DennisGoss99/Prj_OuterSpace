package cga.exercise.components.geometry.particle

import org.joml.*

class Particle(var position : Vector3f, private var velocity : Vector3f, private val lifeLength : Float, var rotation : Float, var scale : Float) {

    private var elapsedTime = 0f

    fun update(dt: Float) : Boolean{

        val movement = Vector3f(scale).mul(dt)

        position.add(movement)

        elapsedTime += dt

        return elapsedTime >= lifeLength
    }



}