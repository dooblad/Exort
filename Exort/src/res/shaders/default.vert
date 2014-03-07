#version 120

varying vec3 color;

void main() {
	gl_TexCoord[0] = gl_MultiTexCoord0;
	color = gl_Color.rgb;
    gl_Position = ftransform();
}