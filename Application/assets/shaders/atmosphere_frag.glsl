#version 330 core


in vec2 textureCoords;

uniform sampler2D texture2D;
uniform vec4 textureColor;
out vec4 color;


void main(void){

    color = normalize(texture(texture2D, textureCoords) * textureColor);

}