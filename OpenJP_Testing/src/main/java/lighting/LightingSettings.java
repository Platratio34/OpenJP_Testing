package lighting;

import java.awt.Color;

import org.joml.Vector3f;

import shaders.ShaderProgram;
import shaders.Uniform;

public class LightingSettings {

	private int ambientColorUniform;
	private int lightPosUniform;
	private int lightColorUniform;
	private int globalLightDirUniform;
	private int globalLightColorUniform;
	
	private Light[] lights;
	private int nextLightId = 0;
	
	private ShaderProgram shader;
	
	public LightingSettings(ShaderProgram shader) {
		ambientColorUniform = shader.getUniform("ambientColor");
		lightPosUniform = shader.getUniform("lightPos");
		lightColorUniform = shader.getUniform("lightColor");
		globalLightDirUniform = shader.getUniform("globalLightDir");
		globalLightColorUniform = shader.getUniform("globalLightColor");
		lights = new Light[16];
		this.shader = shader;
	}
	
	public void setAbientLighting(Color color) {
		Uniform.setColor(ambientColorUniform, color);
	}
	public void setLightPosition(Vector3f pos) {
		Uniform.setVector3f(lightPosUniform, pos);
	}
	public void setLightColor(Color color) {
		Uniform.setColor(lightColorUniform, color);
	}
	public void setGlobalLightDirection(Vector3f pos) {
		Uniform.setVector3f(globalLightDirUniform, pos.normalize());
	}
	public void setGlobalLightColor(Color color) {
		Uniform.setColor(globalLightColorUniform, color);
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
		int posUni = shader.getUniform(uniform+"position");
		Uniform.setVector3f(posUni, light.transform.getPosition());
		int colorUni = shader.getUniform(uniform+"color");
		Uniform.setColor(colorUni, light.getColor());
		int rangeUni = shader.getUniform(uniform+"range");
		Uniform.setFloat(rangeUni, light.getRange());
	}
	
}
