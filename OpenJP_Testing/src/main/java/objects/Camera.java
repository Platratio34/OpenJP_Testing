package objects;

import org.joml.Matrix4f;

import shaders.CameraUBO;
import shaders.ShaderProgram;

/**
 * Camera object
 */
public class Camera implements TransformUpdate {
	
	/** Field of view of the camera (degrees) */
	public float fov = (float)Math.toRadians(60);
	/** Near clip plane depth */
	public float nearZ = 0.01f;
	/** Far clip plane depth */
	public float farZ = 1000f;
	/** Aspect ration (width / height)*/
	public float aspectRatio;
	
	private Matrix4f projectionMatrix;
	
	/** Camera transform */
	public Transform transform;

	/** Camera data UBO */
	public CameraUBO ubo;
	
	@Deprecated
	public Camera(ShaderProgram shader) {
		transform = new Transform();

		ubo = new CameraUBO();
	}
	/**
	 * Create a new camera
	 */
	public Camera() {
		transform = new Transform();

		ubo = new CameraUBO();
	}
	
	/**
	 * Recalculate and store the perspective matrix
	 */
	public void recalculatePerspective() {
		projectionMatrix = new Matrix4f().setPerspective(fov, aspectRatio, nearZ, farZ);
		ubo.setProjectionMatrix(projectionMatrix);
	}
	/**
	 * Recalculate and store the transformation matrix
	 */
	public void recalculateMatrix() {
		ubo.setTransformMatrix(transform.getTransformMatrixInverse());
		ubo.setPosition(transform.getPosition());
	}

	/**
	 * Update the aspect ratio of the camera. Recalculates the perspective matrix.
	 * @param width width of the camera
	 * @param height height of the camera
	 */
	public void updateAspectRatio(int width, int height) {
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
