package test2;

import org.joml.Matrix4f;

import test.ShaderProgram;

public class Renderer {

	private Mesh mesh;
	public Transform transform;
	private ShaderProgram shader;
	
	private int matrixUniform;
	
	public Renderer(Mesh mesh, Transform transform) {
		this.mesh = mesh;
		this.transform = transform;
	}
	
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
		matrixUniform = shader.getUniform("transformMatrix");
	}
	
	public void render() {
		shader.bind();
		Matrix4f matrix = transform.createMatrix();
		Uniform.setMatrix4f(matrixUniform, matrix);
		mesh.render();
	}
}
