package objects;

/**
 * Transform update listener
 */
public interface TransformUpdate {

	/**
	 * Called whenever the transformation matrices are recalculated
	 * @param transform the transform that was updated
	 */
	public void onTransformUpdate(Transform transform);
}
