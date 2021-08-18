package cga.exercise.components.spaceObjects.spaceship

import cga.exercise.components.camera.IPerspective
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

interface IThruster : IRenderable, IPerspective {
    fun activate()
    override fun render(shaderProgram: ShaderProgram)

    override fun cleanup()

    override fun bind(shader: ShaderProgram, projectionMatrix: Matrix4f, viewMatrix: Matrix4f)
    fun update(dt: Float, t: Float)
}