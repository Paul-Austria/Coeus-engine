#version 330 core
out vec4 FragColor;

in vec2 TexCoords;

uniform sampler2D screenTexture;


const float offset = 3.0 / 300.0;
void main()
{
        float Pixelwidth = 1080 *10;
        float PixelHeight = 1080 * 10;
        float dx = 15.0 * (1.0 / Pixelwidth);
        float dy = 10.0 * (1.0 / PixelHeight);
        vec2 Coord = vec2(dx * floor(TexCoords.x / dx),
                          dy * floor(TexCoords.y / dy));

  //      FragColor = texture(screenTexture, Coord);
         FragColor = texture(screenTexture,TexCoords);
}