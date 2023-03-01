#version 310 es
precision mediump float;

layout (location=0) in vec3 position;
layout (location=1) in vec3 color;
layout (location=2) in vec3 normal;

out vec4 vertexColor;
out vec3 vertexNormal;
out vec3 fragPos;

uniform mat4 projectionMatrix;

uniform vec3 cameraPos;
uniform vec3 cameraRot;

uniform mat4 cameraTransform;
uniform mat4 objectTransform;

void main()
{
    gl_Position = vec4(position, 1.0);
    vertexColor = vec4(color.xyz, 1.0);
    vertexNormal = normal;
    fragPos = position;
}