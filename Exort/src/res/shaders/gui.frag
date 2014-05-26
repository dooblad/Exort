#version 120

uniform sampler2D texture;

varying vec2 texCoord;
varying vec4 color;

void main() {
    gl_FragColor = color * texture2D(texture, texCoord);
}