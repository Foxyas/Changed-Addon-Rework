#version 150
in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform vec2 BlurDirection;

const float weights[5] = float[](0.204164, 0.304005, 0.093913, 0.011304, 0.000398);

void main() {
    vec4 result = texture(DiffuseSampler, texCoord) * weights[0];

    for (int i = 1; i < 5; i++) {
        vec2 offset = BlurDirection * i * 1.0 / 256.0;
        result += texture(DiffuseSampler, texCoord + offset) * weights[i];
        result += texture(DiffuseSampler, texCoord - offset) * weights[i];
    }

    fragColor = result;
}
