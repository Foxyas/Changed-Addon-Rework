#version 150

uniform sampler2D DiffuseSampler;
uniform float Threshold;  // alpha mínimo para contar como preenchido
uniform float GapRadius;  // fecha frestas de até (2*raio - 1) pixels. 0 = comportamento vanilla

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

float solid(vec2 uv) {
    return step(Threshold, texture(DiffuseSampler, uv).a);
}

// preenchido, ou vazio com sólido dos dois lados (horizontal ou vertical)
float filled(vec2 uv) {
    if (solid(uv) > 0.5) return 1.0;
    int R = int(GapRadius);
    float l = 0.0, r = 0.0, u = 0.0, d = 0.0;
    for (int i = 1; i <= R; i++) {
        l = max(l, solid(uv - vec2(oneTexel.x * float(i), 0.0)));
        r = max(r, solid(uv + vec2(oneTexel.x * float(i), 0.0)));
        u = max(u, solid(uv - vec2(0.0, oneTexel.y * float(i))));
        d = max(d, solid(uv + vec2(0.0, oneTexel.y * float(i))));
    }
    return max(l * r, u * d);
}

void main() {
    vec2 ox = vec2(oneTexel.x, 0.0);
    vec2 oy = vec2(0.0, oneTexel.y);

    float c  = filled(texCoord);
    float lf = filled(texCoord - ox);
    float rf = filled(texCoord + ox);
    float uf = filled(texCoord - oy);
    float df = filled(texCoord + oy);

    float total = clamp(abs(c - lf) + abs(c - rf) + abs(c - uf) + abs(c - df), 0.0, 1.0);

    vec4 center = texture(DiffuseSampler, texCoord);
    vec4 left   = texture(DiffuseSampler, texCoord - ox);
    vec4 right  = texture(DiffuseSampler, texCoord + ox);
    vec4 up     = texture(DiffuseSampler, texCoord - oy);
    vec4 down   = texture(DiffuseSampler, texCoord + oy);
    vec3 outColor = center.rgb * center.a + left.rgb * left.a + right.rgb * right.a
                  + up.rgb * up.a + down.rgb * down.a;

    fragColor = vec4(outColor * 0.2, total);
}