#version 150

#moj_import <projection.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;  // Coordenada da textura do modelo (Máscara)
in ivec2 UV1; // Overlay Coords (A GPU joga o overlay de dano aqui)
in ivec2 UV2; // Lightmap Coords (A iluminação do mundo vai aqui)
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 texProj0;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    texProj0 = projection_from_position(gl_Position);
    vertexColor = Color;
    texCoord0 = UV0; // Enviando com sucesso a UV para o fragment shader
}