#version 120

uniform sampler2D texture;
varying vec3 color;

void main() {
	gl_FragColor = texture2D(texture, gl_TexCoord[0].st) * vec4(color, 1);
}