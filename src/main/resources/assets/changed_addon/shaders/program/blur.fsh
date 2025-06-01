#version 150

uniform sampler2D DiffuseSampler;

uniform vec2 InSize;
uniform vec2 BlurDir;

in vec2 texCoord;
in vec2 texelSize;

out vec4 fragColor;

#define SQRT_TWO_PI 2.506628274

vec3 BloomSample(vec2 sampleCoord)
{
    return texture(DiffuseSampler, sampleCoord).rgb;
}

#define GAUSSIAN_KERNEL_SIZE 64

vec3 Bloom()
{
    vec2 texelStep = texelSize * BlurDir;

    vec2 centreCoord = texCoord;
    vec3 filteredColor = BloomSample(centreCoord);

    float kernelSampleCount = float(GAUSSIAN_KERNEL_SIZE);

    // Calculate components of the Guassian Function.
    float sigma = kernelSampleCount / 3.0;
    float doubleSigmaSqr = 2 * (sigma * sigma);
    float normalizer = 1.0 / (sigma * SQRT_TWO_PI);

    for(int i = 1; i < GAUSSIAN_KERNEL_SIZE; ++i)
    {
        float weight = exp(((i * i) * -1.0f) / doubleSigmaSqr);
        vec2 offset = texelStep * i;

        filteredColor += BloomSample(centreCoord + offset) * weight;
        filteredColor += BloomSample(centreCoord - offset) * weight;
    }
    filteredColor *= normalizer;

    return filteredColor;
}

void main() {

    vec3 finalColor = Bloom();
    fragColor = vec4(finalColor, 1.0);
}
