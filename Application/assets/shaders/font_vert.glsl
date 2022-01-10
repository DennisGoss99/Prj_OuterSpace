#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 textureCoords;

out vec2 pass_textureCoords;


void main() {

    gl_Position = vec4(position,0.0,1.0);
    pass_textureCoords = textureCoords;
}
