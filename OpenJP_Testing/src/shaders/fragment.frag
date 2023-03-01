#version 310 es
precision mediump float;

in vec4 vertexColor;
in vec3 vertexNormal;
in vec3 fragPos;

out vec4 fragColor;

uniform vec3 ambientColor;
uniform vec3 lightPos;
uniform vec3 lightColor;


void main()
{
	vec3 normal = normalize(vertexNormal);
	vec3 lightDir = normalize(lightPos - fragPos);
	float diff = max(dot(normal, lightPos), 0.0);
	vec3 diffuse = lightColor * diff;
	
	vec3 lighting = ambientColor + diffuse;
	
	fragColor = vertexColor * vec4(lighting.xyz, 1.0);
}