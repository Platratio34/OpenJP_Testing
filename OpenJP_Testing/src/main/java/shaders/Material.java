package shaders;

import java.awt.Color;

public class Material {
	
	private Color color;
	private float smoothness;
	private int textureIndex;
	
	public Material() {
		color = Color.WHITE;
		smoothness = 0.5f;
		textureIndex = -1;
	}
	public Material(Color color) {
		this.color = color;
		smoothness = 0.5f;
		textureIndex = -1;
	}
	public Material(Color color, float smoothness) {
		this.color = color;
		this.smoothness = smoothness;
		textureIndex = -1;
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
	
	public void updateShader(ShaderProgram shader, String uniformName) {
		shader.uniformSetColor4(uniformName+".color", color);
		shader.uniformSetFloat(uniformName+".smoothness", smoothness);
		shader.uniformSetInt1(uniformName+".textureIndex", textureIndex);
	}

	// public int getMatId() {
	// 	return matId;
	// }
}
