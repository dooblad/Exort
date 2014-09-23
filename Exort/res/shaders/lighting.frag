#version 120

uniform mat4 mvMatrix;

uniform vec4 color;

uniform sampler2D texture;
uniform sampler2D normalMap;
uniform bool textured;
uniform bool normalMapped;

uniform vec2 resolution;    
uniform vec3 lightPosition;   
uniform vec4 lightColor;      
uniform vec4 ambientColor;   
uniform vec3 falloff;        

in vec3 position;
in vec3 normal; 
in vec2 texCoord;

out vec4 fragColor;

void main() {
	vec4 diffuseColor;
	vec3 normalColor;
	
	if (textured) {
		diffuseColor = color * texture2D(texture, texCoord);	
		if(normalMapped)
			normalColor = texture2D(normalMap, texCoord).rgb * 2.0 - 1.0;
	} else {
		diffuseColor = color;
	}
	
	vec3 worldLightPosition = vec3((mvMatrix * vec4(lightPosition, 1.0)).xyz);

    float distance = length(position - worldLightPosition);

   	vec3 surfaceToLight = normalize(worldLightPosition - position);
   	
	vec3 diffuse;

    if(normalMapped) {
    	diffuse = (lightColor.rgb * lightColor.a) * max(dot(normalColor, surfaceToLight), 0.0);
    } else {
  		diffuse = (lightColor.rgb * lightColor.a) * max(dot(normal, surfaceToLight), 0.0);
	}
	
    vec3 ambient = ambientColor.rgb * ambientColor.a;

    float attenuation = 1.0 / (falloff.x + (falloff.y * distance) + (falloff.z * distance * distance));

    vec3 intensity = ambient + diffuse * attenuation;
    vec3 finalColor = diffuseColor.rgb * intensity;
    
    fragColor = vec4(finalColor, diffuseColor.a);
}