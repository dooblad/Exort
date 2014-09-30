#version 330

uniform sampler2D texture;

uniform vec4 color;

in vec2 texCoord;

varying out vec4 fragColor;

void main() {
	float alpha = texture2D(texture, texCoord).a;
	
	vec4 finalColor;
	finalColor.rgb = color.rgb;
	finalColor.a = alpha;
	
	if(alpha < 0.5)
		finalColor.a = 0.0;
	else
		finalColor.a = 1.0;
		
	finalColor.a *= color.a;
	finalColor.a *= smoothstep(0.25, 0.75, alpha);
	
   fragColor = finalColor;
}