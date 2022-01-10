package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.SimpleMesh
import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.lwjgl.opengl.GL11

class GuiText (val text : String, val fontSize : Float, val font : FontType, val maxLineLength : Float,
               val centered : Boolean, parent: GuiElement? = null) : Transformable2D(parent = parent ){


    val meshes = mutableListOf<Mesh>()
    //val texCoords = mutableListOf<Float>()
    var cursorX = 0f
    var cursorY = 0f

    init {
        text.forEach { c ->
            setLetter(c)
        }
    }

    private fun setLetter(character : Char){
        val fontTypeChar = font.chars[character.toInt()] ?: throw Exception("Character couldn't be found in $font: [$character]")

        val x = cursorX + fontTypeChar.xOffset * fontSize
        val y = cursorY + fontTypeChar.yOffset * fontSize
        val maxX = x + fontTypeChar.sizeX * fontSize
        val maxY = y + fontTypeChar.sizeY * fontSize
        val properX = (2 * x) - 1
        val properY = (-2 * y) + 1
        val properMaxX = (2 * maxX) - 1
        val properMaxY = (-2 * maxY) + 1

        addVertices(properX, properY, properMaxX, properMaxY, fontTypeChar.xTextureCoord, fontTypeChar.yTextureCoord, fontTypeChar.xMaxTextureCoord, fontTypeChar.yMaxTextureCoord)

        cursorX += fontTypeChar.xAdvance * fontSize
    }

    private fun addVertices(x: Float, y: Float, maxX: Float, maxY: Float, texx: Float, texy: Float, texmaxX: Float, texmaxY: Float) {
        val vao = arrayOf(
            VertexAttribute(2, GL11.GL_FLOAT,16,0),
            VertexAttribute(2, GL11.GL_FLOAT,16,8)
        )
        val ibo = intArrayOf(
            0,1,2,
            2,3,0
        )

        meshes.add(Mesh(floatArrayOf(
            x,y, texx,texy,
            x,maxY, texx,texmaxY,
            maxX,maxY, texmaxX,texmaxY,
            maxX,y, texmaxX,texy
        ), ibo, vao,null))
    }

    fun render(shaderProgram : ShaderProgram){
        //beforeRender()
        //shaderProgram.setUniform("transformationMatrix" , getWorldModelMatrix(),false)
        //mesh.render(shaderProgram)
        //shaderProgram.setUniform("textureCoords",)

        meshes.forEach {
            it.render(shaderProgram)
        }
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        meshes.forEach { it.cleanup()}
    }


}