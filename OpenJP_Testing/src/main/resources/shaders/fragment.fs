#version 310 es
precision mediump float;
#extension GL_ARB_bindless_texture : require

in vec4 vertexColor;
in vec3 vertexNormal;
in vec3 fragPos;
in vec2 textCord;
flat in float matId;

out vec4 fragColor;

uniform vec3 ambientColor;
uniform vec3 globalLightDir;
uniform vec3 globalLightColor;

layout(std140, binding = 1) uniform Camera
{
    mat4 cameraProjection;
    mat4 cameraTransform;
    vec3 cameraPosition;
};

uniform bool unlit;
uniform bool wire;

struct light {
	vec3 position;
	vec3 color;
	float range;
};

uniform light[16] lights;

struct material {
	vec4 color;
	float smoothness;
	bool textured;
	vec2 textureScale;
	vec2 textureOffset;
	layout(bindless_sampler) sampler2D texture;
};

uniform material[16] materials;

layout(bindless_sampler) uniform sampler2D defaultTexture;

void main()
{
	vec4 color = vertexColor;
	float smoothness = 0.0;
	
	if(matId >= 0.0) {
		color = materials[int(matId)].color;
		smoothness = materials[int(matId)].smoothness;
		if(materials[int(matId)].textured && !wire) {
			vec2 uv = textCord + materials[int(matId)].textureOffset;
			uv = uv * materials[int(matId)].textureScale;
			color = color * texture(materials[int(matId)].texture, uv);
		}
	}

	if(unlit || wire) {
		fragColor = color;
		return;
	}
	
	vec3 globalDiffuse = globalLightColor * max(dot(vertexNormal, globalLightDir), 0.0);
	
	vec3 lighting = ambientColor + globalDiffuse;

	for(int i = 0; i < 16; i++) {
		if(lights[i].range <= 0.0) {
			continue;
		}
		vec3 dPos = lights[i].position - fragPos;
		float dist = length(dPos);
		if(lights[i].range < dist) {
			continue;
		}

		// Diffuse
		vec3 dir = normalize(dPos);
		float attn = 1.0 - min(distance(lights[i].position, fragPos)/lights[i].range, 1.0);
		vec3 diff = lights[i].color * max(dot(vertexNormal, dir), 0.0) * attn;

		// Specular
		vec3 viewDir = normalize(cameraPosition - fragPos);
		vec3 reflectDir = reflect(-viewDir, vertexNormal);
		float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
		vec3 specular = lights[i].color * spec;

		lighting = lighting + diff + (specular * smoothness);
	}
	
	fragColor = color * vec4(lighting.xyz, 1.0);
}