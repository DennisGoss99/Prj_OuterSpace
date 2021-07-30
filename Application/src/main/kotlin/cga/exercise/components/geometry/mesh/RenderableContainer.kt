package cga.exercise.components.geometry.mesh

import cga.exercise.components.geometry.IRenderableContainer
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.shader.ShaderProgram

class RenderableContainer(renderables : HashMap< String ,Renderable>) : HashMap<String, Renderable>(renderables),IRenderableContainer {

    override fun render(cameraMode: RenderCategory, shaderProgram: ShaderProgram) {
        super.entries.forEach {
            if(it.value.shouldRender.contains(cameraMode))
                it.value.render(shaderProgram)
        }
    }

    override fun cleanup() {
        super.entries.forEach { it.value.cleanup()}
    }

}