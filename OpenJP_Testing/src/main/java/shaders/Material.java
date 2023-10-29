package shaders;

import java.awt.Color;

public class Material {

	private int matId;
	private Color color;
	private float smoothness;
	private int textureIndex;
	private Materials materials;
	
	public Material(Materials materials) {
		matId = -1;
		color = Color.WHITE;
		smoothness = 0.0f;
		textureIndex = -1;
		this.materials = materials;
	}
	
	public void setColor(Color color) {
		this.color = color;
		materials.updateMaterial(this);
	}
	
	public void updateShader(ShaderProgram shader, String uniformName) {
		shader.uniformSetColor4(uniformName+".color", color);
		shader.uniformSetFloat(uniformName+".smoothness", smoothness);
		shader.uniformSetInt1(uniformName+".textureIndex", textureIndex);
	}

	public int getMatId() {
		return matId;
	}
}
