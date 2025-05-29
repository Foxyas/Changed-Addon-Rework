#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjectionMat;

out vec2 texCoord;

void main() {
    gl_Position = ProjectionMat * ModelViewMat * vec4(Position, 1.0);
    texCoord = UV0;
}
