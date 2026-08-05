#version 150

uniform sampler2D DiffuseSampler; // Main screen
uniform sampler2D BloomSampler;   // Blurred bloom target
uniform float BloomIntensity;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 worldColor = texture(DiffuseSampler, texCoord);
    vec4 bloomColor = texture(BloomSampler, texCoord);

    // Mix the world screen RGB with the bloom light
    vec3 finalColor = worldColor.rgb + (bloomColor.rgb * bloomColor.a * BloomIntensity);

    fragColor = vec4(finalColor, worldColor.a);
}