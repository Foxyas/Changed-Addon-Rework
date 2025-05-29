#version 150

in vec2 texCoord;

uniform vec4 Color;
uniform float Radius;
uniform sampler2D DiffuseSampler;

out vec4 fragColor;

void main() {
    float dist = length(texCoord - vec2(0.5));
    float alpha = 1.0 - smoothstep(0.0, Radius, dist);

    vec4 texColor = texture(DiffuseSampler, texCoord);
    fragColor = vec4(Color.xyz, alpha) * texColor;
}
