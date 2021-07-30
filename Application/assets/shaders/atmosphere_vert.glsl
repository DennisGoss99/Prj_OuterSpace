#version 330 core

layout(location = 0) in vec3 position;
layout(location = 2) in vec3 normal;
layout(location = 1) in vec2 texcoords;

uniform mat4 view_matrix;
uniform mat4 model_matrix;
uniform mat4 projection_matrix;

out vec2 textureCoords;

void main() {

    gl_Position = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);
    textureCoords = texcoords;
}
