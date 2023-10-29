package lighting;

import java.awt.Color;

import objects.Transform;
import objects.TransformUpdate;

public class Light implements TransformUpdate {
	
	public Transform transform;
	
	private Color color;
	private float range;
	
	public int lId = -1;
	public LightingSettings lightSettings;
	
	public Light(Color color, float range, Transform transform) {
		this.color = color;
		this.range = range;
		this.transform = transform;
		transform.addUpdate(this);
	}
	
	public void setColor(Color color) {
		this.color = color;
		updateLight();
	}
	public void setRange(float range) {
		this.range = range;
		updateLight();
	}
	public Color getColor() {
		return color;
	}
	public float getRange() {
		return range;
	}
	
	public void updateLight() {
		lightSettings.updateLight(lId);
	}

	@Override
	public void onTransformUpdate(Transform transform) {
		updateLight();
	}
}
