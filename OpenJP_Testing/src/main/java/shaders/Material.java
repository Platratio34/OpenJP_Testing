package shaders;

import java.awt.Color;

import org.joml.Vector2f;

import objects.Texture2D;

public class Material {
	
	private Color color = Color.WHITE;
	private float smoothness = 0.5f;
	// private int textureIndex;
	private Texture2D texture;
	private Vector2f textureScale =new Vector2f(1, 1);
	private Vector2f textureOffset =new Vector2f(0, 0);
	
	/**
	 * Create a new material.
	 * 
	 * <ul>
	 * <li> Sets color to <code>Color.WHITE</code>.</li>
	 * <li> Sets smoothness to 0.5f.</li>
	 * </ul>
	 */
	public Material() {
		color = Color.WHITE;
		smoothness = 0.5f;
	}
	
	/**
	 * Create a new material.
	 * 
	 * <ul>
	 * <li> Sets smoothness to 0.5f.</li>
	 * </ul>
	 * 
	 * @param color Material color
	 */
	public Material(Color color) {
		this.color = color;
		smoothness = 0.5f;
	}
	
	/**
	 * Create a new material.
	 * @param color Material color
	 * @param smoothness Material smoothness from 0.0f (diffuse only) to 1.0f (specular only)
	 */
	public Material(Color color, float smoothness) {
		this.color = color;
		this.smoothness = smoothness;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}

	public void setSmoothness(float smoothness) {
		this.smoothness = smoothness;
	}
	public float getSmoothness() {
		return smoothness;
	}

	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
	
	public void setTextureScale(Vector2f scale) {
		textureScale = scale;
	}
	public void setTextureScale(float x, float y) {
		textureScale.x = x;
		textureScale.y = y;
	}
	public void setTextureOffset(Vector2f offset) {
		textureOffset = offset;
	}
	public void setTextureOffset(float x, float y) {
		textureOffset.x = x;
		textureOffset.y = y;
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
			int textureScaleUniform = shader.getUniform(uniformName+".textureScale");
        	Uniform.setVector2f(textureScaleUniform, textureScale);
			int textureOffsetUniform = shader.getUniform(uniformName+".textureOffset");
        	Uniform.setVector2f(textureOffsetUniform, textureOffset);
		} else {
			shader.uniformSetInt1(uniformName+".textured", 0);
		}
	}

	// public int getMatId() {
	// 	return matId;
	// }
}
