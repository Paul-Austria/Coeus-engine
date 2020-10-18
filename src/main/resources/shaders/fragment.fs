#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 colour;
uniform int useColour;

void main()
{
    if ( useColour == 1 )
    {
        fragColor = colour;
    }
    else
    {
        fragColor = texture2D(texture_sampler, outTexCoord) * colour;
    }
}