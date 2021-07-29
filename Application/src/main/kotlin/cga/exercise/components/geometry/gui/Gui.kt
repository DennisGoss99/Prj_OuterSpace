package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.IRenderableContainer
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.util.*
import kotlin.collections.HashMap

class Gui(guiElements: HashMap< String , GuiElement>) : HashMap< String , GuiElement>(guiElements), IRenderableContainer {



    override fun render(cameraMode: RenderCategory, shaderProgram: ShaderProgram){
        shaderProgram.use()

        GL30.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_DEPTH_TEST)

        super.entries.forEach {
            if(it.value.shouldRender.contains(cameraMode))
            it.value.render(shaderProgram)
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL30.glDisable(GL11.GL_BLEND)


    }


}