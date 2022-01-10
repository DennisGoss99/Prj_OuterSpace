#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

void main() {

    //out_color = texture(fontAtlas, pass_textureCoords);

    out_color = vec4(1.0f, 1.0f, 1.0f, texture(fontAtlas, pass_textureCoords).a);
}
