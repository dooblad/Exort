#version 120

varying vec3 position;        
varying vec3 normal;
varying vec2 texCoord;
varying vec4 color;

void main() {   
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	position = (gl_ModelViewMatrix * gl_Vertex).xyz;
	
	normal = normalize(gl_NormalMatrix * gl_Normal);
	
	texCoord = gl_MultiTexCoord0.st;
	
	color = gl_Color;
}