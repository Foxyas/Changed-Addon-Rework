#version 150

uniform sampler2D DiffuseSampler;

uniform vec2 InSize;
uniform float BrightnessThreshold;
uniform float IntensityScale;
uniform float IntensityBias;

in vec2 texCoord;
in vec2 texelSize;

out vec4 fragColor;

vec3 rgbChannelWeights = vec3(0.2126, 0.7152, 0.0722);

void main() 
{
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    float brightness = dot(color, rgbChannelWeights);

    // Extracted colors are dimmed based their proximity to the brightness threshold.
    // This fixes patchiness artefacts in the bloom caused by some pixels exceeding the brightness threshold and adjacent pixels not reaching it.
    // This also fixes artefacts related to sky and fog during day time.
    float intensity = max(0.0, pow((brightness - IntensityBias) / BrightnessThreshold, IntensityScale));
    float clampedIntensity = min(1.0, intensity);

    fragColor = vec4(color * (brightness >= BrightnessThreshold ? clampedIntensity : 0.0), 1.0);
}
