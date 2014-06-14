#version 120

uniform sampler2D texture;

varying vec2 texCoord;
varying vec4 color;

void main() {
	// Alpha checking
	if(texture2D(texture, texCoord).a == 0.0)
		discard;
		
    gl_FragColor = color * texture2D(texture, texCoord);
}