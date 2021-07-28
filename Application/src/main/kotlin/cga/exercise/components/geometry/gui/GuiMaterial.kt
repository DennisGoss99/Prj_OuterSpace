package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.material.IMaterial
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D

class GuiMaterial (private val texture: Texture2D) : IMaterial {

    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("guiTexture", 0)
        texture.bind(0)
    }

}