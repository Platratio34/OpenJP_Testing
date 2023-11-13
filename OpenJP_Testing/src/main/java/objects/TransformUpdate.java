package objects;

/**
 * Transform update listener
 */
public interface TransformUpdate {

	/**
	 * Called whenever the transformation matricies are recalculated
	 * @param transform the transform that was updated
	 */
	public void onTransformUpdate(Transform transform);
}
