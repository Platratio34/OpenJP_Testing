package objects;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform implements TransformUpdate {
	
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	
	/**
	 * Parent transform
	 */
	public Transform parent;
	
	private Matrix4f matrix;
	private Matrix4f matrixInverse;
	
	private ArrayList<TransformUpdate> updaters;
	
	public Transform() {
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1.0f);
		matrix = new Matrix4f();
		matrixInverse = new Matrix4f();
		updaters = new ArrayList<TransformUpdate>();
		recalculateMatrix();
	}
	public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		matrix = new Matrix4f();
		matrixInverse = new Matrix4f();
		updaters = new ArrayList<TransformUpdate>();
		recalculateMatrix();
	}
	
	public void recalculateMatrix() {
		matrix.identity().translate(position)
		.rotateX((float)Math.toRadians(rotation.x))
		.rotateY((float)Math.toRadians(rotation.y))
		.rotateZ((float)Math.toRadians(rotation.z))
		.scale(scale);
		
		matrixInverse.identity()
		.rotateX((float)Math.toRadians(rotation.x))
		.rotateY((float)Math.toRadians(rotation.y))
		.rotateZ((float)Math.toRadians(rotation.z))
		.translate(-position.x, -position.y, -position.z);
		
		update();
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
		if (parent != null) {
			Matrix4f out = new Matrix4f();
			parent.getTransformMatrixInverse().mul(matrix, out);
			return out;
		}
		return matrixInverse;
	}
	
	public void setPosition(Vector3f pos) {
		setPosition(pos.x, pos.y, pos.z);
	}
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
		recalculateMatrix();
	}

	/**
	 * Move the transform an offset
	 * @param ammount offset to apply
	 */
	public void translate(Vector3f ammount) {
		translate(ammount.x, ammount.y, ammount.z);
	}
	/**
	 * Move the transform by an offset
	 * @param x x component of offset
	 * @param y y component of offset
	 * @param z z component of offset
	 */
	public void translate(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
		recalculateMatrix();
	}
	
	public void setRotation(Vector3f rot) {
		setRotation(rot.x, rot.y, rot.z);
	}
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
		recalculateMatrix();
	}

	/**
	 * Rotate the transform by an offset
	 * @param ammount offset to rotate by
	 */
	public void rotate(Vector3f ammount) {
		rotate(ammount.x, ammount.y, ammount.z);
	}
	/**
	 * Rotate the transform by an offset
	 * @param x x component of offset
	 * @param y y component of offset
	 * @param z z component of offset
	 */
	public void rotate(float x, float y, float z) {
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
		recalculateMatrix();
	}
	
	public void setScale(Vector3f scale) {
		setScale(scale.x, scale.y, scale.z);
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
	private void update() {
		for(TransformUpdate tu : updaters) {
			tu.onTransformUpdate(this);
		}
	}
	
	@Override
	public void onTransformUpdate(Transform transform) {
		update();
	}
	public void addUpdate(TransformUpdate updater) {
		updaters.add(updater);
	}
	public void removeUpdate(TransformUpdate updater) {
		updaters.remove(updater);
	}

	public String toString() {
		return "Transform: {pos: " + position + ", rot: " + rotation + ", scale: " + scale + "}";
	}
	
	/**
	 * Get the vector representing the forward direction of the transform
	 * @return Identity vector facing positive Z
	 */
	public Vector3f forward() {
		return matrix.normalizedPositiveZ(new Vector3f(1.0f, 0.0f, 0.0f));
		// Vector3f v = new Vector3f();
		// matrix.getRow(2, v);
		// System.out.println(v);
		// v = matrix.normalizedPositiveZ(new Vector3f(1.0f, 0.0f, 0.0f));
		// System.out.println(v);
		// // return v.mul(-1);
		// return v;
	}

	/**
	 * Get the vector representing the right direction of the transform
	 * @return Identity vector facing positive X
	 */
	public Vector3f right() {
		return matrix.normalizedPositiveX(new Vector3f(1.0f, 0.0f, 0.0f));
		// Vector3f v = new Vector3f();
		// matrix.getRow(0, v);
		// return v;
	}

	/**
	 * Get the vector representing the up direction of the transform
	 * @return Identity vector facing positive Y
	 */
	public Vector3f up() {
		return matrix.normalizedPositiveY(new Vector3f(1.0f, 0.0f, 0.0f));
		// Vector3f v = new Vector3f();
		// matrix.getRow(1, v);
		// return v;
	}
}
