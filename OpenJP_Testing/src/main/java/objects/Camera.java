package objects;

import org.joml.Matrix4f;

import shaders.CameraUBO;
import shaders.ShaderProgram;

public class Camera implements TransformUpdate {
	
	public float fov = (float)Math.toRadians(60);
	public float nearZ = 0.01f;
	public float farZ = 1000f;
	public float aspectRatio;
	
	private Matrix4f projectionMatrix;
	
	public Transform transform;

	public CameraUBO ubo;
	
	@Deprecated
	public Camera(ShaderProgram shader) {
		transform = new Transform();

		ubo = new CameraUBO();
	}
	public Camera() {
		transform = new Transform();

		ubo = new CameraUBO();
	}
	
	public void recalculatePerspective() {
		projectionMatrix = new Matrix4f().setPerspective(fov, aspectRatio, nearZ, farZ);
		ubo.setProjectionMatrix(projectionMatrix);
	}
	
	public void recalculateMatrix() {
		ubo.setTransformMatrix(transform.getTransformMatrixInverse());
		ubo.setPosition(transform.getPosition());
	}

	public void updateAspectRation(int width, int height) {
		aspectRatio = (float)width/(float)height;
		recalculatePerspective();
	}

	@Override
	public void onTransformUpdate(Transform transform) {
		recalculateMatrix();
	}

	/**
	 * Bind the camera UBO for rendering
	 */
	public void bindUBO() {ubo.bindTarget();}
}
