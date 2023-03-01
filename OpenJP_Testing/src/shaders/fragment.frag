#version 310 es
precision mediump float;

in vec4 vertexColor;
in vec3 vertexNormal;
in vec3 fragPos;

out vec4 fragColor;

uniform vec3 ambientColor;
uniform vec3 lightPos;
uniform vec3 lightColor;
uniform vec3 globalLightDir;
uniform vec3 globalLightColor;

void main()
{
	vec3 lightDir = normalize(lightPos - fragPos);
	vec3 diffuse = lightColor * max(dot(vertexNormal, lightDir), 0.0);
	
	vec3 globalDiffuse = globalLightColor * max(dot(vertexNormal, globalLightDir), 0.0);
	
	vec3 lighting = ambientColor + diffuse + globalDiffuse;
	
	fragColor = vertexColor * vec4(lighting.xyz, 1.0);
}