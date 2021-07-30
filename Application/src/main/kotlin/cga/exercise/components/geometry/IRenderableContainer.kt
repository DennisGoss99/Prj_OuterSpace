package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram

interface IRenderableContainer {

    fun render(cameraMode: RenderCategory, shaderProgram: ShaderProgram)

    fun cleanup()
}