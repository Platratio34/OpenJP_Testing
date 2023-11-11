#version 310 es
precision mediump float;

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
	int textureIndex;
};

uniform material[16] materials;
// uniform sampler2DArray textures;
// uniform sampler2D textures[16];

uniform sampler2D defaultTexture;

void main()
{
	material cMat = material(vertexColor, 1.0, -1);
	if(matId >= 0.0) {
		cMat = materials[int(matId)];
	}
	
	fragColor = cMat.color;
}