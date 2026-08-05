#version 150

uniform sampler2D DiffuseSampler;

uniform vec2 InSize;
uniform vec2 BlurDir;

in vec2 texCoord;
in vec2 texelSize;

out vec4 fragColor;

#define SQRT_TWO_PI 2.506628274
#define GAUSSIAN_KERNEL_SIZE 32

vec4 BloomSample(vec2 sampleCoord) {
    return texture(DiffuseSampler, sampleCoord);
}

vec4 Bloom() {
    vec2 texelStep = texelSize * BlurDir;
    vec2 centreCoord = texCoord;
    vec4 filteredColor = BloomSample(centreCoord);

    float kernelSampleCount = float(GAUSSIAN_KERNEL_SIZE);
    float sigma = kernelSampleCount / 3.0;
    float doubleSigmaSqr = 2.0 * (sigma * sigma);
    float normalizer = 1.0 / (sigma * SQRT_TWO_PI);

    for(int i = 1; i < GAUSSIAN_KERNEL_SIZE; ++i) {
        float weight = exp(((i * i) * -1.0) / doubleSigmaSqr);
        vec2 offset = texelStep * i;

        filteredColor += BloomSample(centreCoord + offset) * weight;
        filteredColor += BloomSample(centreCoord - offset) * weight;
    }

    return filteredColor * normalizer;
}

void main() {
    fragColor = Bloom();
}