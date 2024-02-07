#version 310 es
#extension GL_ARB_bindless_texture : require
precision mediump float;

in vec4 vertexColor;
in vec3 vertexNormal;
in vec3 fragPos;
in vec2 textCord;
flat in float matId;

out vec4 fragColor;

layout(std140, binding = 1) uniform Camera
{
    mat4 cameraProjection;
    mat4 cameraTransform;
    vec3 cameraPosition;
};

void main()
{
    vec4 groundColor = vec4(0.1, 0.05, 0.0, 1.0);
    vec4 horizonColor = vec4(0.2, 0.3, 0.1, 1.0);
    vec4 skyColor = vec4(0.2, 0.7, 0.9, 1.0);

    vec3 dir = normalize(fragPos);
    vec4 color = vec4(1.0, 1.0, 0.0, 0.0);
    if(dir.y <= -0.1) {
        color = mix(groundColor, horizonColor, ((dir.y+0.1)*2.0)+1.0);
    } else {
        color = mix(horizonColor, skyColor, (dir.y+0.1)*2.0);
    }
    // vec4 color = mix(horizonColor, skyColor, (dir.y/2.0)+0.5);
    // float b = dir.y;
    // float r = 0;
    // float g = 0;
	// vec4 color = vec4(0.0, 0.0, b, 1.0);
	
	fragColor = color;
    // fragColor = vec4(dir.xyz,1.0)*-1.0;
    // fragColor = vec4(dir.y, 0.0, 0.0, 1.0) + vec4(0.0, 0.0, -dir.y, 1.0);
    // fragColor = vec4(textCord.x,0.0,textCord.y,1.0);
    // fragColor = vec4((fragPos - cameraPosition), 1.0) * 0.001;
}