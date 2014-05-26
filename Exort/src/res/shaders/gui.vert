#version 120

varying vec2 texCoord;
varying vec4 color;

void main() {   
	gl_Position = gl_ModelViewMatrix * gl_Vertex;
	
	texCoord = gl_MultiTexCoord0.st;
	
	color = gl_Color;
}