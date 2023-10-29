#version 310 es
precision mediump float;

in vec4 vertexColor;
in vec3 vertexNormal;
in vec3 fragPos;
in vec2 textCord;
in float matId;

out vec4 fragColor;

uniform vec3 ambientColor;
uniform vec3 lightPos;
uniform vec3 lightColor;
uniform vec3 globalLightDir;
uniform vec3 globalLightColor;

struct light {
	vec3 position;
	vec3 color;
	float range;
};

uniform light[16] lights;

struct material {
	vec4 color;
	float smoothness;
	// sampler2D texture;
	int textureIndex;
};

uniform material[16] materials;
// uniform sampler2DArray textures;
// uniform sampler2D textures[16];

uniform sampler2D defaultTexture;

// vec4 getSampleFromArray(sampler2D textures[16], int ndx, vec2 uv) {
//     vec4 color = vec4(0);
//     for (int i = 0; i < 16; ++i) {
//       vec4 c = texture2D(textures[i], uv);
//       if (i == ndx) {
//         color += c;
//       }
//     }
//     return color;
// }

void main()
{
	material cMat = material(vertexColor, 0.0, -1);
	if(matId >= 0.0) {
		cMat = materials[int(matId)];
	}
	
	vec3 lightDir = normalize(lightPos - fragPos);
	vec3 diffuse = lightColor * max(dot(vertexNormal, lightDir), 0.0);
	
	vec3 globalDiffuse = globalLightColor * max(dot(vertexNormal, globalLightDir), 0.0);
	
	vec3 lighting = ambientColor + diffuse + globalDiffuse;
	
	for(int i = 0; i < 16; i++) {
		vec3 dir = normalize(lights[i].position - fragPos);
		float attn = 1.0 - min(distance(lights[i].position, fragPos)/lights[i].range, 1.0);
		vec3 diff = lights[i].color * max(dot(vertexNormal, dir), 0.0) * attn;
		lighting = lighting + diff;
	}
	
	vec4 color = cMat.color;
	
	// color = cMat.color * texture(cMat.texture, textCord);
	if(cMat.textureIndex >= 0) {
		// color = cMat.color * texture(textures[cMat.textureIndex], textCord);
		// color = cMat.color * getSampleFromArray(textures, cMat.textureIndex, textCord);
	} else {
		color = cMat.color * texture(defaultTexture, textCord);
	}
	
	
	fragColor = vertexColor * vec4(lighting.xyz, 1.0);
}