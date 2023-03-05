package test2;

import org.joml.Matrix4f;
import test.ShaderProgram;

public class Camera {
	
	public float fov = (float)Math.toRadians(60);
	public float nearZ = 0.01f;
	public float farZ = 1000f;
	public float aspectRatio;
	
	private Matrix4f projectionMatrix;
	private int projectionMatrixUniform;
	
	public Transform transform;
	private int transformMatrixUniform;
	
	public Camera(ShaderProgram shader) {
		transform = new Transform();
		
		projectionMatrixUniform = shader.getUniform("projectionMatrix");
		transformMatrixUniform = shader.getUniform("cameraMatrix");
	}
	
	public void recaculatePerspective() {
		projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, nearZ, farZ);
		Uniform.setMatrix4f(projectionMatrixUniform, projectionMatrix);
	}
	
	public void recalculateMatrix() {
		Uniform.setMatrix4f(transformMatrixUniform, transform.getTransformMatrixInverse());
	}

	public void updateAspectRation(int width, int height) {
		aspectRatio = (float)width/(float)height;
		recaculatePerspective();
	}
}
