#version 120

uniform sampler2D texture;

varying vec2 texCoord;
varying vec4 color;

void main() {
	float alpha = texture2D(texture, texCoord).a;
	
	vec4 finalColor;
	finalColor.rgb = color.rgb;
	
	if(alpha < 0.5)
		finalColor.a = 0.0;
	else
		finalColor.a = 1.0;
		
	finalColor.a *= smoothstep(0.25, 0.75, alpha);
	
    gl_FragColor = finalColor;
}