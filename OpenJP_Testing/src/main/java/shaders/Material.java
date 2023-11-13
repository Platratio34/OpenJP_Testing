package shaders;

import java.awt.Color;

import org.joml.Vector2f;

import objects.Texture2D;

/**
 * Rendering material <br>
 * <br>
 * Tracks color, smoothness, and texture<br>
 * Shader independent.
 * <br>
 * <b>Defaults</b>
 * <ul>
 * <li>color: <code>Color.WHITE</code></li>
 * <li>smoothness: <code>0.5f</code></li>
 * <li>texture: <code>null</code></li>
 * <li>textureScale: <code>(1.0f, 1.0f)</code></li>
 * <li>textureOffset: <code>(0.0f, 0.0f)</code></li>
 * </ul>
 */
public class Material {
	
	private Color color = Color.WHITE;
	private float smoothness = 0.5f;
	// private int textureIndex;
	private Texture2D texture;
	private Vector2f textureScale = new Vector2f(1, 1);
	private Vector2f textureOffset = new Vector2f(0, 0);
	
	/**
	 * Create a new material.
	 * 
	 * <ul>
	 * <li> Sets color to <code>Color.WHITE</code>.</li>
	 * <li> Sets smoothness to <code>0.5f</code>.</li>
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
	 * <li> Sets smoothness to <code>0.5f</code>.</li>
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
	 * @param smoothness Material smoothness from <code>0.0f</code> (diffuse only) to <code>1.0f</code> (specular only)
	 */
	public Material(Color color, float smoothness) {
		this.color = color;
		this.smoothness = smoothness;
	}
	
	/**
	 * Set the base color of the material
	 * @param color base color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * Get the base color of the material
	 * @return base color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the material smoothness
	 * @param smoothness smoothness from <code>0.0f</code> (diffuse only) to <code>1.0f</code> (specular only)
	 */
	public void setSmoothness(float smoothness) {
		this.smoothness = smoothness;
	}
	/**
	 * Get the material smoothness
	 * @return Smoothness from <code>0.0f</code> (diffuse only) to <code>1.0f</code> (specular only)
	 */
	public float getSmoothness() {
		return smoothness;
	}

	/**
	 * Set the material color texture
	 * @param texture
	 */
	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
	/**
	 * Get the current color texture of the material
	 * @return Current texture, may be <code>null</code>
	 */
	public Texture2D getTexture() {
		return texture;
	}

	/**
	 * Set the scale of the texture (multiped by uv coordinate)
	 * @param scale texture scale
	 */
	public void setTextureScale(Vector2f scale) {
		textureScale = scale;
	}
	/**
	 * Set the scale of the texture (multiped by uv coordinate)
	 * @param x x scale of texture
	 * @param y y scale of texture
	 */
	public void setTextureScale(float x, float y) {
		textureScale.x = x;
		textureScale.y = y;
	}
	/**
	 * Get the current scale of the texture
	 * @return texture scale
	 */
	public Vector2f getTextureScale() {
		return textureScale;
	}

	/**
	 * Set the offset of the texture (applied before scaling)
	 * @param offset texture offset
	 */
	public void setTextureOffset(Vector2f offset) {
		textureOffset = offset;
	}
	/**
	 * Set the offset of the texture (applied before scaling)
	 * @param x x offset of texture
	 * @param y y offset of texture
	 */
	public void setTextureOffset(float x, float y) {
		textureOffset.x = x;
		textureOffset.y = y;
	}
	/**
	 * Get the current offset of the texture
	 * @return texture offset
	 */
	public Vector2f getTextureOffset() {
		return textureOffset;
	}
	
	/**
	 * Update shader uniforms for this material
	 * @param shader shader to work in
	 * @param uniformName uniform name for the material (ie. <code>materials[1]</code>)
	 * @param index material index
	 */
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
}
