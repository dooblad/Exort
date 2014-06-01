#version 120

uniform mat4 modelViewMatrix;

uniform sampler2D texture;
uniform bool textured;

uniform vec2 resolution;    
uniform vec3 lightPosition;   
uniform vec4 lightColor;      
uniform vec4 ambientColor;   
uniform vec3 falloff;        

varying vec3 position;
varying vec3 normal; 
varying vec2 texCoord;
varying vec4 color;

void main() {
	vec4 diffuseColor;
	
	if (textured) {
		diffuseColor = color * texture2D(texture, texCoord);	
	} else {
		diffuseColor = color;
	}
	
	vec3 worldLightPosition = vec3((modelViewMatrix * vec4(lightPosition, 1.0)).xyz);

    float distance = length(position - worldLightPosition);

   	vec3 surfaceToLight = normalize(worldLightPosition - position);

    vec3 diffuse = (lightColor.rgb * lightColor.a) * max(dot(normal, surfaceToLight), 0.0);

    vec3 ambient = ambientColor.rgb * ambientColor.a;

    float attenuation = 1.0 / (falloff.x + (falloff.y * distance) + (falloff.z * distance * distance));

    vec3 intensity = ambient + diffuse * attenuation;
    vec3 finalColor = diffuseColor.rgb * intensity;
    
    gl_FragColor = vec4(finalColor, diffuseColor.a);
}