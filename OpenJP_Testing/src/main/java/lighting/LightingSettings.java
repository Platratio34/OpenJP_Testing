package lighting;

import java.awt.Color;

import org.joml.Vector3f;

import shaders.ShaderProgram;
import shaders.Uniform;

public class LightingSettings {

	private Uniform ambientColorUniform;
	private Uniform lightPosUniform;
	private Uniform lightColorUniform;
	private Uniform globalLightDirUniform;
	private Uniform globalLightColorUniform;
	
	private Light[] lights;
	private int nextLightId = 0;
	
	private ShaderProgram shader;
	
	public LightingSettings(ShaderProgram shader) {
		ambientColorUniform = new Uniform(shader, "ambientColor");
		lightPosUniform = new Uniform(shader, "lightPos");
		lightColorUniform = new Uniform(shader, "lightColor");
		globalLightDirUniform = new Uniform(shader, "globalLightDir");
		globalLightColorUniform = new Uniform(shader, "globalLightColor");
		lights = new Light[16];
		this.shader = shader;
	}
	
	public void setAbientLighting(Color color) {
		ambientColorUniform.setColor(color);
	}
	public void setLightPosition(Vector3f pos) {
		lightPosUniform.setVector3f(pos);
	}
	public void setLightColor(Color color) {
		lightColorUniform.setColor(color);
	}
	public void setGlobalLightDirection(Vector3f pos) {
		globalLightDirUniform.setVector3f(pos.normalize());
	}
	public void setGlobalLightColor(Color color) {
		globalLightColorUniform.setColor(color);
	}

	public int addLight(Light light) {
		if(nextLightId >= lights.length) return -1;
		int id = nextLightId;
		nextLightId++;
		lights[id] = light;
		light.lId = id;
		light.lightSettings = this;
		setLightArray("lights", lights[id], id);
		return id;
	}
	public void setLight(int id, Light light) {
		if(id < 0 || id >= lights.length) throw new IndexOutOfBoundsException("ID must be in range 0 < id < "+lights.length+"; ID was "+id);
		lights[id].lId = -1;
		lights[id].lightSettings = null;
		if(light == null) return;
		lights[id] = light;
		light.lId = id;
		light.lightSettings = this;
		setLightArray("lights", lights[id], id);
	}
	public void updateLight(int id) {
		if(id < 0 || id >= lights.length) throw new IndexOutOfBoundsException("ID must be in range 0 < id < "+lights.length+"; ID was "+id);
		if(lights[id] == null) throw new NullPointerException("Light at ID must be non-null; ID was "+id);
		setLightArray("lights", lights[id], id);
	}
	public Light getLight(int id) {
		if(id < 0 || id >= lights.length) throw new IndexOutOfBoundsException("ID must be in range 0 < id < "+lights.length+"; ID was "+id);
		return lights[id];
	}
	private void setLightArray(String uniform, Light light, int index) {
		uniform += "["+index+"].";
		Uniform posUni = new Uniform(shader, uniform+"position");
		posUni.setVector3f(light.transform.getPosition());
		Uniform colorUni = new Uniform(shader, uniform+"color");
		colorUni.setColor(light.getColor());
		Uniform rangeUni = new Uniform(shader, uniform+"range");
		rangeUni.setFloat(light.getRange());
	}
	
}
