package lighting;

import java.awt.Color;

import org.joml.Vector3f;

import shaders.ShaderProgram;
import shaders.Uniform;

/**
 * Lighting settings for shader.<br>
 * <br>
 * <b>Shader specific</b>
 */
public class LightingSettings {

	/** Maximum number of lights in environment */
	public static final int MAX_LIGHTS = 16;

	private Uniform ambientColorUniform;
	private Uniform lightPosUniform;
	private Uniform lightColorUniform;
	private Uniform globalLightDirUniform;
	private Uniform globalLightColorUniform;

	private Light[] lights;

	private ShaderProgram shader;

	/**
	 * Create a new lighting settings for the given shader
	 * 
	 * @param shader shader to use
	 */
	public LightingSettings(ShaderProgram shader) {
		ambientColorUniform = new Uniform(shader, "ambientColor");
		lightPosUniform = new Uniform(shader, "lightPos");
		lightColorUniform = new Uniform(shader, "lightColor");
		globalLightDirUniform = new Uniform(shader, "globalLightDir");
		globalLightColorUniform = new Uniform(shader, "globalLightColor");
		lights = new Light[MAX_LIGHTS];
		this.shader = shader;
	}

	/**
	 * Set the ambient light color
	 * 
	 * @param color ambient color
	 */
	public void setAmbientLighting(Color color) {
		ambientColorUniform.setColor(color);
	}

	@Deprecated
	public void setLightPosition(Vector3f pos) {
		lightPosUniform.setVector3f(pos);
	}

	@Deprecated
	public void setLightColor(Color color) {
		lightColorUniform.setColor(color);
	}

	/**
	 * Set the global directional light direction
	 * 
	 * @param pos global light direction
	 */
	public void setGlobalLightDirection(Vector3f pos) {
		globalLightDirUniform.setVector3f(pos.normalize());
	}

	/**
	 * Set the global directional light color
	 * 
	 * @param color global light color
	 */
	public void setGlobalLightColor(Color color) {
		globalLightColorUniform.setColor(color);
	}

	/**
	 * Add a light to the environment
	 * 
	 * @param light the light to add
	 * @return Light ID OR <code>-1</code> if there was no free slot for a light
	 */
	public int addLight(Light light) {
		int id = -1;
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (lights[i] == null) {
				id = i;
				break;
			}
		}
		if (id < 0) {
			return -1;
		}
		lights[id] = light;
		light.lId = id;
		light.setLightingSettings(this);
		setLightArray("lights", lights[id], id);
		return id;
	}

	/**
	 * Set light in specific slot
	 * 
	 * @param id    slot id. Must be less than <code>MAX_LIGHTS</code>
	 * @param light light to set
	 */
	public void setLight(int id, Light light) {
		if (id < 0 || id >= MAX_LIGHTS)
			throw new IndexOutOfBoundsException("ID must be in range 0 < id < " + MAX_LIGHTS + "; ID was " + id);
		lights[id].lId = -1;
		lights[id].setLightingSettings(null);
		if (light == null)
			return;
		lights[id] = light;
		light.lId = id;
		light.setLightingSettings(this);
		setLightArray("lights", lights[id], id);
	}

	/**
	 * Update light in shader
	 * 
	 * @param id light slot. Must be less than <code>MAX_LIGHTS</code>
	 */
	public void updateLight(int id) {
		if (id < 0 || id >= MAX_LIGHTS)
			throw new IndexOutOfBoundsException("ID must be in range 0 < id < " + MAX_LIGHTS + "; ID was " + id);
		if (lights[id] == null)
			throw new NullPointerException("Light at ID must be non-null; ID was " + id);
		setLightArray("lights", lights[id], id);
	}

	/**
	 * Get the light in slot
	 * 
	 * @param id light slot. Must be less than <code>MAX_LIGHTS</code>
	 * @return Selected light
	 */
	public Light getLight(int id) {
		if (id < 0 || id >= MAX_LIGHTS)
			throw new IndexOutOfBoundsException("ID must be in range 0 < id < " + MAX_LIGHTS + "; ID was " + id);
		return lights[id];
	}

	private void setLightArray(String uniform, Light light, int index) {
		uniform += "[" + index + "].";
		Uniform posUni = new Uniform(shader, uniform + "position");
		posUni.setVector3f(light.transform.getPosition());
		Uniform colorUni = new Uniform(shader, uniform + "color");
		colorUni.setColor(light.getColor());
		Uniform rangeUni = new Uniform(shader, uniform + "range");
		rangeUni.setFloat(light.getRange());
	}

}
