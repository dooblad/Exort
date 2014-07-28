#version 120

uniform sampler2D texture;

varying vec4 color;
varying vec2 texCoord;

void main() {
    gl_FragColor = texture2D(texture, texCoord) * color;
}