package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

class Gui(var guiElements: List<GuiElement>) : IRenderable {



    override fun render(shaderProgram: ShaderProgram){
        shaderProgram.use()

        GL30.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_DEPTH_TEST)

        guiElements.forEach { it.render(shaderProgram) }

        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL30.glDisable(GL11.GL_BLEND)


    }


}