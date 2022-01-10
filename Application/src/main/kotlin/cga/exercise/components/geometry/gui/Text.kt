package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.IRenderableContainer
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30

class Text (var textElements: HashMap< FontType , List<GuiText>>) : IRenderableContainer {

    override fun render(cameraModes: List<RenderCategory>, shaderProgram: ShaderProgram) {
        shaderProgram.use()

        GL30.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_DEPTH_TEST)

        textElements.forEach {
            it.value.forEach { text->
                text.render(shaderProgram)
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL30.glDisable(GL11.GL_BLEND)
    }

    override fun cleanup() {
        textElements.forEach { t, u ->
            u.forEach { g ->
                g.cleanup()
            }
        }
    }


}