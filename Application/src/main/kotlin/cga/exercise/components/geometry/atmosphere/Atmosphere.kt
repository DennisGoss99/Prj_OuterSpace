package cga.exercise.components.geometry.atmosphere

import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.material.SimpleMaterial
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

class Atmosphere ( val shouldRender : List<RenderCategory>,
                   simpleMaterial: SimpleMaterial ,
                   parent: Transformable? = null) : RenderableBase(
                        (ModelLoader.loadModel("assets/models/atmosphereSphere.obj",0f,0f,0f) ?: throw Exception("Couldn't find Object: 'atmosphereSphere.obj'")).meshes,
                        parent = parent) {
    init {
        meshes[0].material = simpleMaterial
    }

    override fun render(shaderProgram: ShaderProgram) {


        shaderProgram.use()

        GL30.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        //GL11.glDisable(GL11.GL_DEPTH_TEST)

        super.render(shaderProgram)

        //GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL30.glDisable(GL11.GL_BLEND)

    }

}