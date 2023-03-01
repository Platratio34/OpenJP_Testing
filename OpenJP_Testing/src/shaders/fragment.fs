#version 310 es

precision mediump float;

in vec4 vertexColor;
in vec3 vertexNormal;
in vec3 fragPos;

out vec4 fragColor;

uniform vec4 ambientColor;
uniform vec3 lightPos;
uniform vec4 lightColor;

void main()
{
	vec3 normal = normalize(vertexNormal);
	vec3 lightDir = normalize(lightPos - fragPos);
	float diff = max(dot(norm, lightPos), 0.0);
	vec3 diffuse = diff * lightColor;
	
	fragColor = (ambientColor + diffuse) * vertexColor;
}