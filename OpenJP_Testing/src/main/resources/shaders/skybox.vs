#version 310 es
precision mediump float;

layout(location=0) in vec3 position;
layout(location=1) in vec3 color;
layout(location=2) in vec3 normal;
layout(location=3) in vec2 uv;

out vec4 vertexColor;
out vec3 vertexNormal;
out vec3 fragPos;
out vec2 textCord;
flat out float matId;

uniform mat4 transformMatrix;

layout(std140, binding = 1) uniform Camera
{
    mat4 cameraProjection;
    mat4 cameraTransform;
    vec3 cameraPosition;
};

uniform vec3[8] colors;
uniform bool useColor;

void main()
{
    vec4 pos = cameraProjection * inverse(cameraTransform) * vec4(position+cameraPosition, 1.0);
    // pos.z = pos.w;
    // pos = pos / pos.w;
    // pos.z = 0.9;
    // pos.w = 1.0;
    gl_Position = pos;
    textCord = vec2(pos.z, 0.0);
    if(useColor) {
    	matId = -1.0;
    } else {
    	matId = color.x;
    }
    vertexColor = vec4(color, 1.0);
    vertexNormal = normalize((vec4(normal, 0.0)).xyz);
    fragPos = (vec4(position, 1.0)).xyz;
    fragPos = fragPos;
    // textCord = uv;
}