package test2;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f scale;
	
	public Transform parent;
	
	public Transform() {
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1.0f);
	}
	public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Matrix4f createMatrix() {
		Matrix4f world = new Matrix4f();
		world.identity().translate(position)
				.rotateX((float)Math.toRadians(rotation.x))
				.rotateY((float)Math.toRadians(rotation.y))
				.rotateZ((float)Math.toRadians(rotation.z))
				.scale(scale);
		if(parent != null) world = parent.createMatrix().mul(world);
		return world;
	}
	public Matrix4f createMatrixInvert() {
		Matrix4f world = new Matrix4f();
		world.identity().translate(-position.x, -position.y, -position.z)
				.rotateX((float)Math.toRadians(rotation.x))
				.rotateY((float)Math.toRadians(rotation.y))
				.rotateZ((float)Math.toRadians(rotation.z))
				.scale(scale);
		if(parent != null) world = world.mul(parent.createMatrixInvert());
		return world;
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}
	public void setScale(float x, float y, float z) {
		scale.x = x;
		scale.y = y;
		scale.z = z;
	}
}
