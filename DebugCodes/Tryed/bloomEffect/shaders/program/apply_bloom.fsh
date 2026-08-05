#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D BloomSampler;

uniform vec2 InSize;
uniform float BloomIntensity;

in vec2 texCoord;
in vec2 texelSize;

out vec4 fragColor;

void main() 
{
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    float colorIntensity = length(color) * 0.8; // Used to reduce bloom intensity on already bright areas (such as snow during the day).

    vec3 bloom = texture(BloomSampler, texCoord).rgb * BloomIntensity;

    vec3 finalColor = color + (bloom * pow(0.08, colorIntensity));
    fragColor = vec4(finalColor, 1.0);
}
