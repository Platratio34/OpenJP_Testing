#version 310 es
precision mediump float;

layout(location=0) in vec3 position;
layout(location=1) in vec3 color;
layout(location=2) in vec3 normal;

out vec4 vertexColor;
out vec3 vertexNormal;
out vec3 fragPos;

uniform mat4 projectionMatrix;
uniform mat4 cameraMatrix;
uniform mat4 transformMatrix;

uniform vec3[8] colors;
uniform bool useColors;

void main()
{
    gl_Position = projectionMatrix * cameraMatrix * transformMatrix * vec4(position, 1.0);
    if(useColors) {
    	vertexColor = vec4(colors[int(color.x)], 1.0);
    } else {
    	vertexColor = vec4(color, 1.0);
    }
    vertexNormal = normalize((transformMatrix * vec4(normal, 0.0)).xyz);
    fragPos = (transformMatrix * vec4(position, 1.0)).xyz;
}