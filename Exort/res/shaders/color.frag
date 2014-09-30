#version 330

uniform vec4 color;

varying out vec4 fragColor;

void main() {
    fragColor = color;
}