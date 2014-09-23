#version 330

uniform mat4 mvpMatrix;

in vec4 inPosition;
in vec3 inNormal;
in vec2 inTexCoord;

out vec2 texCoord;
out vec4 color;

void main() {   
	gl_Position = mvpMatrix * inPosition;
	
	texCoord = inTexCoord;
}