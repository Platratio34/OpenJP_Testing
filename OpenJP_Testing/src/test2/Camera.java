package test2;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import test.ShaderProgram;
import vectorLibrary.Vector3D;

public class Camera {
	
	private Vector3D position;
	private int posUniform;
	private Vector3D rotation;
	private int rotUniform;
	
	public float fov = (float)Math.toRadians(60);
	public float nearZ = 0.01f;
	public float farZ = 1000f;
	public float aspectRatio;
	
	private Matrix4f projectionMatrix;
	private int projectionMatrixUniform;
	
	public Camera(ShaderProgram shader) {
		posUniform = shader.getUniform("cameraPos");
		rotUniform = shader.getUniform("cameraRot");
		
		projectionMatrixUniform = shader.getUniform("projectionMatrix");
	}
	
	public void setPosition(Vector3D pos) {
		position = pos;
//		GL33.glUniform3f(posUniform, (float)pos.x, (float)pos.y, (float)pos.z);
	}
	public void setPosition(float x, float y, float z) {
		position = new Vector3D(x,y,z);
//		GL33.glUniform3f(posUniform, x, y, z);
	}
	public void setRotation(Vector3D rot) {
		rotation = rot;
//		GL33.glUniform3f(rotUniform, (float)rot.x, (float)rot.y, (float)rot.z);
	}
	
	public Vector3D getPosition() {
		return position;
	}
	public Vector3D getRotation() {
		return rotation;
	}
	
	public void recaculateTransform() {
		projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, nearZ, farZ);
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			projectionMatrix.get(fb);
			GL33.glUniformMatrix4fv(projectionMatrixUniform, false, projectionMatrix);
		}
	}
}
