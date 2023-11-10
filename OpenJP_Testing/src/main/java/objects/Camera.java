package objects;

import org.joml.Matrix4f;

import shaders.CameraUBO;
import shaders.ShaderProgram;
import shaders.Uniform;

public class Camera implements TransformUpdate {
	
	public float fov = (float)Math.toRadians(60);
	public float nearZ = 0.01f;
	public float farZ = 1000f;
	public float aspectRatio;
	
	private Matrix4f projectionMatrix;
	private Uniform projectionMatrixUniform;
	
	public Transform transform;
	private Uniform transformMatrixUniform;
	private Uniform posUniform;

	private CameraUBO ubo;
	
	public Camera(ShaderProgram shader) {
		transform = new Transform();
		
		projectionMatrixUniform = new Uniform(shader, "projectionMatrix");
		transformMatrixUniform = new Uniform(shader, "cameraMatrix");
		posUniform = new Uniform(shader, "cameraPos");

		ubo = new CameraUBO();
	}
	
	public void recalculatePerspective() {
		// projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, nearZ, farZ);
		projectionMatrix = new Matrix4f().setPerspective(fov, aspectRatio, nearZ, farZ);
		projectionMatrixUniform.setMatrix4f(projectionMatrix);
		ubo.setProjectionMatrix(projectionMatrix);
	}
	
	public void recalculateMatrix() {
		transformMatrixUniform.setMatrix4f(transform.getTransformMatrixInverse());
		ubo.setTransformMatrix(transform.getTransformMatrixInverse());
		posUniform.setVector3f(transform.getPosition());
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

	public void bindUBO() {ubo.bind();}
	public void unbindUBO() {ubo.unbind();}
}
