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
	
	private Uniform matrixUniform;
	private Uniform colorsUniform;
	private Uniform useColorsUniform;

	private boolean visible = true;
	
	public Renderer(Mesh mesh, Transform transform) {
		this.mesh = mesh;
		this.transform = transform;
	}
	
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
		matrixUniform = new Uniform(shader, "transformMatrix");
		colorsUniform = new Uniform(shader, "colors");
		useColorsUniform = new Uniform(shader, "useColors");
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
		matrixUniform.setMatrix4f(matrix);
		if(colors != null) {
			colorsUniform.setColorArray(colors);
			useColorsUniform.setBoolean(true);
		} else {
			useColorsUniform.setBoolean(false);
		}
		mesh.render();
	}
}
