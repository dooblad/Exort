uniform sampler2D texture;
uniform sampler2D normalMap;

varying vec3 normal;
varying vec2 texCoord;

//values used for shading algorithm...
uniform vec3 lightPosition;   //light position, normalized
uniform vec4 lightColor;      //light RGBA -- alpha is intensity
uniform vec4 ambientColor;    //ambient RGBA -- alpha is intensity 
uniform vec3 falloff;         //attenuation coefficients

void main() {   
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	normal = gl_NormalMatrix * gl_Normal;
	
	texCoord = gl_MultiTexCoord0.st;
}