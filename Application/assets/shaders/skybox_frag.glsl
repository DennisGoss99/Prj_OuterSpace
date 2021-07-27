#version 330 core

in vec3 textureCoords;
out vec4 color;

uniform samplerCube cubeMap;

void main(void){

//    if(textureCoords == vec3(0.0f,0.0f,0.0f))
//        color = vec4(1.0f,0.0f,0.0f,0.0f);
//    color = vec4(1.0f,1.0f,0.0f,0.0f);
    color = texture(cubeMap, textureCoords);//texture(cubeMap, vec3(0.5f,0.5f,0.0f));//
}