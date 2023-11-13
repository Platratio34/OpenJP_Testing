package lighting;

import java.awt.Color;

import objects.Transform;
import objects.TransformUpdate;

/**
 * Graphical light
 */
public class Light implements TransformUpdate {
	
	/** Transformation of the light */
	public Transform transform;
	
	private Color color;
	private float range;
	
	/** Light id */
	public int lId = -1;
	private LightingSettings lightSettings;
	
	/**
	 * Create a new light
	 * @param color light color
	 * @param range light range
	 * @param transform light transformation
	 */
	public Light(Color color, float range, Transform transform) {
		this.color = color;
		this.range = range;
		this.transform = transform;
		transform.addUpdate(this);
	}

	/**
	 * Set the lighting settings this light is rendered under
	 * @param lS lighting settings object
	 */
	public void setLightingSettings(LightingSettings lS) {
		lightSettings = lS;
	}
	
	/**
	 * Set the color of the light
	 * @param color new color
	 */
	public void setColor(Color color) {
		this.color = color;
		updateLight();
	}
	/**
	 * Get the current color of the light
	 * @return Current light color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the range of the light
	 * @param range new range
	 */
	public void setRange(float range) {
		this.range = range;
		updateLight();
	}
	/**
	 * Get the current range of the light
	 * @return Current light range
	 */
	public float getRange() {
		return range;
	}
	
	/**
	 * Update the light in the asscoiated lighting settings
	 */
	public void updateLight() {
		lightSettings.updateLight(lId);
	}

	@Override
	public void onTransformUpdate(Transform transform) {
		updateLight();
	}
}
