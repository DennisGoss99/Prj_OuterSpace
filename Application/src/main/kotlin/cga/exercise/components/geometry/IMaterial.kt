package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram

interface IMaterial {
    fun bind(shaderProgram: ShaderProgram)
}