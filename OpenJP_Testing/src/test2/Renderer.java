package test2;

import org.joml.Matrix4f;

import java.awt.Color;
import test.ShaderProgram;

public class Renderer {

	private Mesh mesh;
	public Transform transform;
	private ShaderProgram shader;
	
	public Color[] colors;
	
	private int matrixUniform;
	private int colorsUniform;
	private int useColorsUniform;
	
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
	
	public void render() {
		shader.bind();
		Matrix4f matrix = transform.createMatrix();
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
