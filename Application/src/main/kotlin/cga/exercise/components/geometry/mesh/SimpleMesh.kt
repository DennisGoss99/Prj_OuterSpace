package cga.exercise.components.geometry.mesh

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.material.IMaterial
import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class SimpleMesh(val vertexdata: FloatArray, val texturedata : FloatArray, private val vertexcount : Int, attributes: Array<VertexAttribute>, var material: IMaterial?) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var texvbo = 0



    init {
        // generate IDs
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        texvbo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, texvbo)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, texturedata, GL30.GL_STATIC_DRAW)

        vbo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexdata, GL30.GL_STATIC_DRAW)



        for(i in 0 until attributes.size){
            GL30.glEnableVertexAttribArray(i)
            GL30.glVertexAttribPointer(i, attributes[i].size, attributes[i].type, false, attributes[i].stride, attributes[i].offset.toLong())
        }

        //Unbind
        GL30.glBindVertexArray(0)
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0)
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    /**
     * renders the mesh
     */
    private fun render() {
        // activate VAO
        GL30.glBindVertexArray(vao)

        // render call
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL30.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexcount)
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
        // call the rendering method every frame
    }

    fun render(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        material?.bind(shaderProgram)
        render()
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)

        material?.cleanup()
    }
}