#version 420 core

in vec2 passTextureCoord;

out vec4 outcolor;

uniform sampler2D tex;
uniform vec3 ambientLight;

void main() {
    outcolor = vec4(ambientLight, 1.0) * texture(tex, passTextureCoord);
}