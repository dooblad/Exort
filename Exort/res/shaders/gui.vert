#version 330

uniform mat4 mvpMatrix;

in vec4 inPosition;
in vec2 inTexCoord;

out vec2 texCoord;

void main() {   
	gl_Position = mvpMatrix * inPosition;
	
	texCoord = inTexCoord;
}