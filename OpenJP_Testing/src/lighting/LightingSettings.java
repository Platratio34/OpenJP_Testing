package lighting;

import java.awt.Color;

import org.joml.Vector3f;

import GLObjects.Uniform;
import shaders.ShaderProgram;

public class LightingSettings {

	private int ambientColorUniform;
	private int lightPosUniform;
	private int lightColorUniform;
	private int globalLightDirUniform;
	private int globalLightColorUniform;
	
	public LightingSettings(ShaderProgram shader) {
		ambientColorUniform = shader.getUniform("ambientColor");
		lightPosUniform = shader.getUniform("lightPos");
		lightColorUniform = shader.getUniform("lightColor");
		globalLightDirUniform = shader.getUniform("globalLightDir");
		globalLightColorUniform = shader.getUniform("globalLightColor");
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
	
}
