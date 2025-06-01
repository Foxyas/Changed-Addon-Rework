#version 150
in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    float brightness = dot(color.rgb, vec3(0.2126, 0.7152, 0.0722)); // luminância
    if (brightness > 0.2) {
        fragColor = vec4(color.rgb, 1.0);
    } else {
        fragColor = vec4(0.0);
    }
}
