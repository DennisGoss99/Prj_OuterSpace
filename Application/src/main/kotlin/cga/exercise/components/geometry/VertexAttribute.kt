package cga.exercise.components.geometry

/**
 * Simple class that holds all information about a single vertex attribute
 */
/**
 * Creates a VertexAttribute object
 * @param size         Number of components of this attribute
 * @param type      Type of this attribute
 * @param stride    Size in bytes of a whole vertex
 * @param offset    Offset in bytes from the beginning of the vertex to the location of this attribute data
 */
data class VertexAttribute(var size: Int, var type: Int, var stride: Int, var offset: Int)

