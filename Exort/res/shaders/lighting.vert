#version 330

uniform mat4 mvpMatrix;
uniform mat4 mvMatrix;

in vec4 inPosition;
in vec3 inNormal;
in vec2 inTexCoord;

out vec3 position;        
out vec3 normal;
out vec2 texCoord;

void main() {   
	gl_Position = mvpMatrix * inPosition;
	
	position = (mvMatrix * inPosition).xyz;
	
	normal = normalize((mvMatrix * vec4(inNormal, 0.0)).xyz);
	
	texCoord = inTexCoord;
}