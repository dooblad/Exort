#version 330

uniform sampler2D texture;

uniform vec4 color;

in vec2 texCoord;

varying out vec4 fragColor;

void main() {
	vec4 textureColor = texture2D(texture, texCoord);

	// Alpha checking
	if(textureColor.a == 0.0)
		discard;
	vec4 finalColor = color * textureColor;
    fragColor = finalColor;
}