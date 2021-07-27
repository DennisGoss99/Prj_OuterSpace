#version 330 core

in vec3 textureCoords;
out vec4 color;

uniform samplerCube cubeMap;

void main(void){
    color = vec4(1.0f,0.0f,0.0f,0.0f);//texture(cubeMap, textureCoords);
}