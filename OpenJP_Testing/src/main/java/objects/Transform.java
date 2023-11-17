package objects;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
	private Transform parent;
	
	private Matrix4f matrix;
	private Matrix4f matrixCamera;
	
	private ArrayList<TransformUpdate> updaters;

	/** If this is a transform for a camera */
	public boolean isCamera;
	
	/**
	 * Create a new transform.
	 */
	public Transform() {
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1.0f);
		matrix = new Matrix4f();
		matrixCamera = new Matrix4f();
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
		matrixCamera = new Matrix4f();
		updaters = new ArrayList<TransformUpdate>();
		recalculateMatrix();
	}
	
	/**
	 * Recalculate transformation matrices.<br>
	 * <br>
	 * <b>Notifies all listeners</b>
	 */
	public void recalculateMatrix() {
		if (isCamera) {
			matrix.identity();
			matrix.translate(position);
			matrix.rotateX((float)Math.toRadians(-rotation.x));
			matrix.rotateY((float)Math.toRadians(-rotation.y));
		} else {
			matrix.identity()
			.translate(position)
			.rotateX((float)Math.toRadians(rotation.x))
			.rotateY((float)Math.toRadians(rotation.y))
			.rotateZ((float)Math.toRadians(rotation.z))
			.scale(scale);
		}
		
		// matrix.invertAffine(matrixInverse);
		matrixCamera.identity();
		matrixCamera.translate(position.x, position.y, position.z);
		matrixCamera.rotateY((float)Math.toRadians(-rotation.y));
		matrixCamera.rotateX((float)Math.toRadians(-rotation.x));
		// matrixCamera.transpose();
		// matrixCamera.invertAffine();
		
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
			if (isCamera) {
				parent.getTransformMatrix().mul(matrix, out);
			} else {
				parent.getTransformMatrixCamera().mul(matrix, out);
			}
			return out;
		}
		return matrix;
	}

	/**
	 * Get the inverse transformation matrix for camera
	 * @return inverse transformation matrix
	 */
	public Matrix4f getTransformMatrixCamera() {
		if (parent != null) {
			Matrix4f out = new Matrix4f();
			parent.getTransformMatrixCamera().mul(matrixCamera, out);
			return out;
		}
		return matrixCamera;
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
	 * Get the world space position of the transform
	 * @return
	 */
	public Vector3f getWorldPosition() {
		Vector3f pos = new Vector3f(position);
		if (parent != null) {
			Matrix4f pMatrix = parent.getTransformMatrix();
			if (isCamera)
				pMatrix = parent.getTransformMatrixCamera();
			Vector4f vec4 = new Vector4f(pos.x, pos.y, pos.z, 1.0f).mul(pMatrix);
			pos.x = vec4.x;
			pos.y = vec4.y;
			pos.z = vec4.z;
		}
		return pos;
	}

	public Vector3f getPosition(Vector3f offset) {
		Matrix4f mat = getTransformMatrix();
		Vector4f pos = new Vector4f(offset.x, offset.y, offset.z, 1.0f).mul(mat);
		return new Vector3f(pos.x, pos.y, pos.z);
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

	public void setParent(Transform parent) {
		if (this.parent != null)
			this.parent.removeUpdate(this);
		this.parent = parent;
		if (this.parent != null)
			parent.addUpdate(this);
	}

	public Transform getParent() {
		return parent;
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
