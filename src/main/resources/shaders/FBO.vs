#version 330 core
layout (location = 0) in vec2 Pos;
layout (location = 1) in vec2 Tex;

out vec2 TexCoords;

void main()
{
    TexCoords = Tex;
    gl_Position = vec4(Pos.x, Pos.y, 0.0, 1.0);
}