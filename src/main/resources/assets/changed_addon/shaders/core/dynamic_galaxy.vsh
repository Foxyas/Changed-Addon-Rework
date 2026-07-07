#version 150

#moj_import <projection.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;  // Texture coordinate of the model (Mask)
in ivec2 UV1; // Overlay Coords (The GPU passes damage flash overlay here)
in ivec2 UV2; // Lightmap Coords (The world illumination goes here)
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
    texCoord0 = UV0; // Successfully sending the UV to the fragment shader
}