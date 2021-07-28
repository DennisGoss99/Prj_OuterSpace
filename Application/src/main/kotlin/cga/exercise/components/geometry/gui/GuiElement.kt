package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*
import javax.swing.Spring.scale


class GuiElement(path : String, var scale : Vector2f = Vector2f(1f) , var translate : Vector2f  = Vector2f(0f), parent: Transformable? = null) : Transformable2D(parent = parent ){

    private val mesh : Mesh

    init{

        val VBO = floatArrayOf(
            -1f, 1f,
            -1f,-1f,
            1f, 1f,
            1f,-1f)

        val VAO = arrayOf(
            VertexAttribute(2, GL11.GL_FLOAT,8,0)
        )

        val IBO= intArrayOf(
            3, 2, 1,
            0, 1, 2
        )

        var tex = Texture2D(path,false).setTexParams(GL_CLAMP_TO_EDGE,GL_CLAMP_TO_EDGE, GL_LINEAR, GL_LINEAR)


        var material = GuiMaterial(tex)

        mesh = Mesh(VBO, IBO, VAO, material)
    }

    fun render(shaderProgram: ShaderProgram){

        translateLocal(translate)
        scaleLocal(scale)
        rotateLocal(0f)

        shaderProgram.setUniform("transformationMatrix" , getWorldModelMatrix(),false)
        mesh.render(shaderProgram)
    }


}