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
out float matId;

uniform mat4 projectionMatrix;
uniform mat4 cameraMatrix;
uniform mat4 transformMatrix;

layout(std140, binding = 0) uniform Camera
{
    mat4 cameraProjection;
    mat4 cameraTransform;
    vec3 cameraPosition;
};

uniform vec3[8] colors;
uniform bool useColor;

void main()
{
    gl_Position = projectionMatrix * cameraMatrix * transformMatrix * vec4(position, 1.0);
    // gl_Position = cameraProjection * cameraTransform * transformMatrix * vec4(position, 1.0);
    if(useColor) {
    	matId = -1.0;
    } else {
    	matId = color.x;
    }
    vertexColor = vec4(color, 1.0);
    vertexNormal = normalize((transformMatrix * vec4(normal, 0.0)).xyz);
    fragPos = (transformMatrix * vec4(position, 1.0)).xyz;
    textCord = uv;
}