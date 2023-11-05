package objects;

import org.joml.Matrix4f;

import shaders.Materials;
import shaders.ShaderProgram;
import shaders.Uniform;

public class Renderer {

	private Mesh mesh;
	public Transform transform;
	private ShaderProgram shader;
	
	private Uniform matrixUniform;
	private Uniform useColorsUniform;

	private boolean visible = true;

	public Materials materials;
	
	public Renderer(Mesh mesh, Transform transform) {
		this.mesh = mesh;
		this.transform = transform;
		materials = new Materials();
	}
	
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
		matrixUniform = new Uniform(shader, "transformMatrix");
		useColorsUniform = new Uniform(shader, "useColor");
		materials.setShader(shader);
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
		if(materials.getMaterial(0) != null) {
			// colorsUniform.setColorArray(colors);
			useColorsUniform.setBoolean(false);
			materials.bind();
		} else {
			useColorsUniform.setBoolean(true);
		}
		mesh.render();
	}
}
