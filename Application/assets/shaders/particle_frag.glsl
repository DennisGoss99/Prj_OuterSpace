#version 330 core

in vec2 textureCoords;

out vec4 color;
uniform sampler2D texture2D;
uniform float blend;

void main(void){

    color = texture(texture2D,textureCoords + 200 * 4);

}
