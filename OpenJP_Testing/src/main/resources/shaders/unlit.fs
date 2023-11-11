#version 310 es
precision mediump float;
#extension GL_ARB_bindless_texture : require

in vec4 vertexColor;
in vec3 vertexNormal;
in vec3 fragPos;
in vec2 textCord;
in float matId;

out vec4 fragColor;

layout(std140, binding = 1) uniform Camera
{
    mat4 cameraProjection;
    mat4 cameraTransform;
    vec3 cameraPosition;
};

struct material {
	vec4 color;
	float smoothness;
	// sampler2D texture;
	// int textureIndex;
	bool textured;
	uvec2 texture;
};

uniform material[16] materials;
// uniform sampler2DArray textures;
// uniform sampler2D textures[16];

uniform sampler2D defaultTexture;

void main()
{
	vec4 color = vertexColor;
	
	if(matId >= 0.0) {
		// material cMat = materials[int(matId)];
		color = materials[int(matId)].color;
		// if(materials[int(matId)].textured && !wire) {
		// 	// color = texture(materials[int(matId)].texture, textCord);
		// 	color = vec4(textCord.x, textCord.y, 0.0, 1.0);
		// }
	}
	
	fragColor = color;
}