#version 330 core

in vec3 position;
out vec3 textureCoords;

uniform mat4 view_matrix;
uniform mat4 projection_matrix;

void main(void){

    // Viewmatrix doesnt contain transformations!
    mat4 view_matrix_withoutTranslation = view_matrix;
    view_matrix_withoutTranslation[3].xyz = vec3 (0.0f, 0.0f, 0.0f);

    gl_Position = projection_matrix * view_matrix_withoutTranslation * vec4(position, 1.0);
    textureCoords = position;

}