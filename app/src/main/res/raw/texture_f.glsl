#version 300 es

in highp vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
layout(location = 0) out vec4 outColor;

void main()
{
    outColor = texture(inputImageTexture, textureCoordinate);
}