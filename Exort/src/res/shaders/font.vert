#version 120

uniform bool threeD;

varying vec2 texCoord;
varying vec4 color;

void main() {   
	if(threeD)
		gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	else
		gl_Position = gl_ModelViewMatrix * gl_Vertex;
	
	texCoord = gl_MultiTexCoord0.st;
	
	color = gl_Color;
}