package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.material.SimpleMaterial
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import java.util.*

class GuiText (var text : String, var fontSize : Float, val font : FontType, val maxLineLength : Float,
               val centered : Boolean, translate : Vector2f = Vector2f(0f,0f), roll : Float = 0f, parent: GuiElement? = null) : Transformable2D(parent = parent ){

    private var mesh : Mesh
    private var cursorX = 0f
    private var cursorY = 0f

    private var vertexData = mutableListOf<Float>()
    private var iboData = mutableListOf<Int>()
    private var iboCursor = 0

    var color = Vector3f(1f,1f,1f)

    private val vao = arrayOf(
        VertexAttribute(2, GL11.GL_FLOAT,16,0),
        VertexAttribute(2, GL11.GL_FLOAT,16,8)
    )

    init {
        text.forEach { c ->
            setLetter(c, fontSize /3)
        }

        mesh = Mesh(vertexData.toFloatArray(),iboData.toIntArray(),vao, font.fontImageMaterial)

        translateLocal(translate)
        rotateLocal(roll)
    }

    private fun setLetter(character : Char, fontSize : Float){
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
        Collections.addAll(vertexData,
        x, y, texx, texy,
        x, maxY, texx, texmaxY,
        maxX, maxY, texmaxX, texmaxY,
        maxX, y, texmaxX, texy)

        Collections.addAll(iboData,
            iboCursor, iboCursor+1, iboCursor+2,
            iboCursor+2,iboCursor+3,iboCursor)

        iboCursor += 4
    }

    fun textHasChanged(){
        mesh.cleanupMesh()

        vertexData.clear()
        iboData.clear()
        iboCursor = 0
        cursorX = 0f
        cursorY = 0f

        text.forEach { c ->
            setLetter(c, fontSize /3)
        }

        mesh = Mesh(vertexData.toFloatArray(),iboData.toIntArray(),vao, font.fontImageMaterial)
    }

    fun render(shaderProgram : ShaderProgram){
        shaderProgram.setUniform("transformationMatrix" , getWorldModelMatrix(),false)
        shaderProgram.setUniform("color" , color)
        mesh.render(shaderProgram)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        mesh.cleanup()
    }


}