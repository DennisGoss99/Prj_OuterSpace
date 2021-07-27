package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*
import javax.swing.Spring.scale


class GuiElement(path : String, var scale : Vector2f = Vector2f(1f) , var translate : Vector2f  = Vector2f(0f)) {

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

        var tex = Texture2D(path,false)
        tex.setTexParams(GL_CLAMP_TO_EDGE,GL_CLAMP_TO_EDGE, GL_LINEAR, GL_LINEAR)

        var material = GuiMaterial(tex)

        mesh = Mesh(VBO, IBO, VAO, material)
    }

    fun render(shaderProgram: ShaderProgram){
        val matrix = Matrix4f()
        matrix.translate(Vector3f(translate,0f))
        matrix.scale(Vector3f(scale, 1f))

        shaderProgram.setUniform("transformationMatrix" , matrix,false)
        mesh.render(shaderProgram)
    }


}