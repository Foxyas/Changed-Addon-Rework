#version 150

uniform sampler2D DiffuseSampler;         // Frame atual
uniform sampler2D PreviousFrameSampler;   // Frame anterior
uniform float blurAmount;                 // Intensidade do blur (0~1)
//uniform vec2 InSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 current = texture(DiffuseSampler, texCoord);
    vec4 previous = texture(PreviousFrameSampler, texCoord);
    fragColor = mix(current, previous, blurAmount); // mistura suavizada
}
