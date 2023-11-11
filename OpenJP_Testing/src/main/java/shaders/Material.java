package shaders;

import java.awt.Color;

import objects.Texture2D;

public class Material {
	
	private Color color;
	private float smoothness;
	// private int textureIndex;
	private Texture2D texture;
	
	public Material() {
		color = Color.WHITE;
		smoothness = 0.5f;
	}
	public Material(Color color) {
		this.color = color;
		smoothness = 0.5f;
	}
	public Material(Color color, float smoothness) {
		this.color = color;
		this.smoothness = smoothness;
	}
	
	public void setColor(Color color) {
		this.color = color;
		// materials.updateMaterial(this);
	}
	public Color getColor() {
		return color;
	}

	public void setSmoothness(float smoothness) {
		this.smoothness = smoothness;
	}
	public float getSmoothnes() {
		return smoothness;
	}

	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
	
	public void updateShader(ShaderProgram shader, String uniformName, int index) {
		shader.uniformSetColor4(uniformName+".color", color);
		shader.uniformSetFloat(uniformName+".smoothness", smoothness);
		// shader.uniformSetInt1(uniformName+".textureIndex", textureIndex);
		if(texture != null) {
			// texture.updateTexture();
			int textureUniform = shader.getUniform(uniformName+".texture");
        	Uniform.setTexture2D(textureUniform, texture);
			shader.uniformSetInt1(uniformName+".textured", 1);
		} else {
			shader.uniformSetInt1(uniformName+".textured", 0);
		}
	}

	// public int getMatId() {
	// 	return matId;
	// }
}
