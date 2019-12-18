#version 420 core

in vec3 position;
in vec2 textureCoords;

out vec2 passTextureCoord;

uniform mat4 model;

void main() {
    gl_Position = model * vec4(position, 1.0);
    passTextureCoord = textureCoords;
}