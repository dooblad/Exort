#version 330

uniform mat4 mvpMatrix;

in vec4 inPosition;

void main() {   
	gl_Position = mvpMatrix * inPosition;
}