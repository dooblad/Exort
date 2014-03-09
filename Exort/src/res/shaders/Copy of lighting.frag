//attributes from vertex shader
varying vec3 normal;
varying vec2 texCoord;

uniform sampler2D texture;    //diffuse map

//values used for shading algorithm...
uniform vec2 resolution;      //resolution of screen
uniform vec3 lightPosition;   //light position, normalized
uniform vec4 lightColor;      //light RGBA -- alpha is intensity
uniform vec4 ambientColor;    //ambient RGBA -- alpha is intensity 
uniform vec3 falloff;         //attenuation coefficients

void main() {
    //RGBA of our diffuse color
    //vec4 diffuseColor = texture2D(texture, texCoord);
	vec4 diffuseColor = vec4(1.0, 1.0, 1.0, 1.0);

    //Determine distance (used for attenuation) BEFORE we normalize our lightDirection
    float D = length(lightPosition);

    //normalize our vectors
    vec3 L = normalize(lightPosition);

    //Pre-multiply light color with intensity
    //Then perform "N dot L" to determine our diffuse term
    vec3 diffuse = (lightColor.rgb * lightColor.a) * max(dot(normal, L), 0.0);

    //pre-multiply ambient color with intensity
    vec3 ambient = ambientColor.rgb * ambientColor.a;

    //calculate attenuation
    float attenuation = 1.0 / ( falloff.x + (falloff.y*D) + (falloff.z*D*D) );

    //the calculation which brings it all together
    vec3 intensity = ambient + diffuse * attenuation;
    vec3 finalColor = diffuseColor.rgb * intensity;
    gl_FragColor = vec4(finalColor, diffuseColor.a);
}