package test2;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	
	public Transform parent;
	
	private Matrix4f matrix;
	private Matrix4f matrixInverse;
	
	public Transform() {
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1.0f);
		matrix = new Matrix4f();
		matrixInverse = new Matrix4f();
		recalculateMatrix();
	}
	public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		matrix = new Matrix4f();
		matrixInverse = new Matrix4f();
		recalculateMatrix();
	}
	
	public void recalculateMatrix() {
		matrix.identity().translate(position)
		.rotateX((float)Math.toRadians(rotation.x))
		.rotateY((float)Math.toRadians(rotation.y))
		.rotateZ((float)Math.toRadians(rotation.z))
		.scale(scale);
		
		matrixInverse.identity().translate(-position.x, -position.y, -position.z)
		.rotateX((float)Math.toRadians(rotation.x))
		.rotateY((float)Math.toRadians(rotation.y))
		.rotateZ((float)Math.toRadians(rotation.z))
		.scale(scale);
	}
	
	public Matrix4f getTransformMatrix() {
//		return createMatrix();
		if(parent != null) {
			Matrix4f out = new Matrix4f();
			parent.getTransformMatrix().mul(matrix, out);
			return out;
		}
		return matrix;
	}
	public Matrix4f getTransformMatrixInverse() {
		if(parent != null) {
			Matrix4f out = new Matrix4f();
			parent.getTransformMatrixInverse().mul(matrix, out);
			return out;
		}
		return matrixInverse;
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
		recalculateMatrix();
	}
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
		recalculateMatrix();
	}
	public void setScale(float x, float y, float z) {
		scale.x = x;
		scale.y = y;
		scale.z = z;
		recalculateMatrix();
	}
	
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	public Vector3f getRotation() {
		return new Vector3f(rotation);
	}
	public Vector3f getScale() {
		return new Vector3f(scale);
	}
}
