package cga.exercise.components.geometry.skybox

import cga.exercise.components.geometry.Mesh
import java.util.jar.Attributes
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11.*


class SkyboxRenderer{

private val SIZE = 3f

private val mesh : Mesh

    private val VBO = floatArrayOf(
    -SIZE, SIZE, -SIZE,
    SIZE, SIZE, -SIZE,
    -SIZE, SIZE, SIZE,
    SIZE, SIZE, SIZE,
    -SIZE, -SIZE, -SIZE,
    SIZE, -SIZE, -SIZE,
    -SIZE, -SIZE, SIZE,
    SIZE, -SIZE, SIZE
)

    private val VAO = arrayOf(
        VertexAttribute(3, GL_FLOAT,12,0)
    )

    private val IBO= intArrayOf(
        5, 1, 0,
        4, 5, 0,
        5, 3, 1,
        5, 7, 3,
        7, 6, 3,
        6, 2, 3,
        4, 0, 2,
        6, 4, 2,
        7, 5, 4,
        4, 6, 7,
        1, 3, 0,
        3, 2, 0
    )

    init {
        mesh = Mesh(VBO,IBO,VAO, null)
    }

    fun render(shaderProgram: ShaderProgram){
        shaderProgram.use()
        mesh.render()
    }


}
