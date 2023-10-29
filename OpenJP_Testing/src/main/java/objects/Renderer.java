package objects;

import org.joml.Matrix4f;

import shaders.ShaderProgram;
import shaders.Uniform;

import java.awt.Color;

public class Renderer {

	private Mesh mesh;
	public Transform transform;
	private ShaderProgram shader;
	
	public Color[] colors;
	
	private int matrixUniform;
	private int colorsUniform;
	private int useColorsUniform;

	private boolean visible = true;
	
	public Renderer(Mesh mesh, Transform transform) {
		this.mesh = mesh;
		this.transform = transform;
	}
	
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
		matrixUniform = shader.getUniform("transformMatrix");
		colorsUniform = shader.getUniform("colors");
		useColorsUniform = shader.getUniform("useColors");
	}
	
	public void setColors(Color[] colors) {
		this.colors = colors;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void render() {
		if (!visible) return;
		shader.bind();
		Matrix4f matrix = transform.getTransformMatrix();
		Uniform.setMatrix4f(matrixUniform, matrix);
		if(colors != null) {
			Uniform.setColorArray(colorsUniform, colors);
			Uniform.setBoolean(useColorsUniform, true);
		} else {
			Uniform.setBoolean(useColorsUniform, false);
		}
		mesh.render();
	}
}
