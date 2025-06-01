#version 150
in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform sampler2D Bloom;

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);
    vec4 bloom = texture(Bloom, texCoord);
    fragColor = base + bloom * 1.3; // Intensifica o efeito
}
