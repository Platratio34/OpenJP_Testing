package objects;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Transformation object for position, rotation, and scale
 */
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
	
	/**
	 * Create a new transform.
	 */
	public Transform() {
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1.0f);
		matrix = new Matrix4f();
		matrixInverse = new Matrix4f();
		updaters = new ArrayList<TransformUpdate>();
		recalculateMatrix();
	}
	/**
	 * Create a new transform
	 * @param position position of the transform
	 * @param rotation rotation of the transform
	 * @param scale scale of the transform
	 */
	public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		matrix = new Matrix4f();
		matrixInverse = new Matrix4f();
		updaters = new ArrayList<TransformUpdate>();
		recalculateMatrix();
	}
	
	/**
	 * Recalculate transformation matrices.<br>
	 * <br>
	 * <b>Notifies all listeners</b>
	 */
	public void recalculateMatrix() {
		matrix.identity().translate(position)
		.rotateX((float)Math.toRadians(rotation.x))
		.rotateY((float)Math.toRadians(rotation.y))
		.rotateZ((float)Math.toRadians(rotation.z))
		.scale(scale);
		
		// matrix.invertAffine(matrixInverse);
		matrixInverse.identity()
		.rotateX((float)Math.toRadians(rotation.x))
		.rotateY((float)Math.toRadians(rotation.y))
		.rotateZ((float)Math.toRadians(rotation.z))
		.translate(-position.x, -position.y, -position.z);
		
		update();
	}
	
	/**
	 * Get the transformation matrix
	 * @return transformation matrix
	 */
	public Matrix4f getTransformMatrix() {
//		return createMatrix();
		if(parent != null) {
			Matrix4f out = new Matrix4f();
			parent.getTransformMatrix().mul(matrix, out);
			return out;
		}
		return matrix;
	}

	/**
	 * Get the inverse transformation matrix for camera
	 * @return inverse transformation matrix
	 */
	public Matrix4f getTransformMatrixInverse() {
		if (parent != null) {
			Matrix4f out = new Matrix4f();
			parent.getTransformMatrixInverse().mul(matrix, out);
			return out;
		}
		return matrixInverse;
	}
	
	/**
	 * Set the position of the transform. <b>Recalculates the matrix</b>
	 * @param pos new position
	 */
	public void setPosition(Vector3f pos) {
		setPosition(pos.x, pos.y, pos.z);
	}
	/**
	 * Set the position of the transform. <b>Recalculates the matrix</b>
	 * @param x new x position
	 * @param y new y position
	 * @param z new z position
	 */
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
		recalculateMatrix();
	}

	/**
	 * Move the transform an offset. <b>Recalculates the matrix</b>
	 * @param amount offset to apply
	 */
	public void translate(Vector3f amount) {
		translate(amount.x, amount.y, amount.z);
	}
	/**
	 * Move the transform by an offset. <b>Recalculates the matrix</b>
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
	
	/**
	 * Set the rotation of the transform. <b>Recalculates the matrix</b>
	 * @param pos new rotation
	 */
	public void setRotation(Vector3f rot) {
		setRotation(rot.x, rot.y, rot.z);
	}
	/**
	 * Set the rotation of the transform. <b>Recalculates the matrix</b>
	 * @param x new x rotation
	 * @param y new y rotation
	 * @param z new z rotation
	 */
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
		recalculateMatrix();
	}

	/**
	 * Rotate the transform by an offset. <b>Recalculates the matrix</b>
	 * @param amount offset to rotate by
	 */
	public void rotate(Vector3f amount) {
		rotate(amount.x, amount.y, amount.z);
	}
	/**
	 * Rotate the transform by an offset. <b>Recalculates the matrix</b>
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
	
	/**
	 * Set the scale of the transform. <b>Recalculates the matrix</b>
	 * @param pos new scale
	 */
	public void setScale(Vector3f scale) {
		setScale(scale.x, scale.y, scale.z);
	}
	/**
	 * Set the scale of the transform. <b>Recalculates the matrix</b>
	 * @param x new x scale
	 * @param y new y scale
	 * @param z new z scale
	 */
	public void setScale(float x, float y, float z) {
		scale.x = x;
		scale.y = y;
		scale.z = z;
		recalculateMatrix();
	}
	
	/**
	 * Get the current position of the transform
	 * @return current position
	 */
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	/**
	 * Get the current rotation of the transform
	 * @return current rotation
	 */
	public Vector3f getRotation() {
		return new Vector3f(rotation);
	}
	/**
	 * Get the current scale of the transform
	 * @return current scale
	 */
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
	/**
	 * Add an update listener
	 * @param updater listener
	 */
	public void addUpdate(TransformUpdate updater) {
		updaters.add(updater);
	}
	/**
	 * Removes an update listener
	 * @param updater listener
	 */
	public void removeUpdate(TransformUpdate updater) {
		updaters.remove(updater);
	}

	/**
	 * Returns a string representation of the transform, containing the position, rotation, and scale
	 */
	public String toString() {
		return "Transform: {pos: " + position + ", rot: " + rotation + ", scale: " + scale + "}";
	}
	
	/**
	 * Get the vector representing the forward direction of the transform
	 * @return Identity vector facing positive Z
	 */
	public Vector3f forward() {
		return matrix.normalizedPositiveZ(new Vector3f(1.0f, 0.0f, 0.0f)).negate();
	}

	/**
	 * Get the vector representing the right direction of the transform
	 * @return Identity vector facing positive X
	 */
	public Vector3f right() {
		return matrix.normalizedPositiveX(new Vector3f(1.0f, 0.0f, 0.0f));
	}

	/**
	 * Get the vector representing the up direction of the transform
	 * @return Identity vector facing positive Y
	 */
	public Vector3f up() {
		return matrix.normalizedPositiveY(new Vector3f(1.0f, 0.0f, 0.0f));
	}
}
