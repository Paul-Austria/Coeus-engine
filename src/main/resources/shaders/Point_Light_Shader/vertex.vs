#version 330 core
layout (location=0) in vec3 position;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform vec4 colour;

out vec4 bColour;

void main()
{
        bColour = colour;
	    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
}