package test2;

import java.awt.Color;

import org.lwjgl.opengl.GL33;

import test.ShaderProgram;
import vectorLibrary.Vector3D;

public class LightingSettings {

	private ShaderProgram shader;
	private int ambientColorUniform;
	private int lightPosUniform;
	private int lightColorUniform;
	
	public LightingSettings(ShaderProgram shader) {
		this.shader = shader;
		ambientColorUniform = shader.getUniform("ambientColor");
		lightPosUniform = shader.getUniform("lightPos");
		lightColorUniform = shader.getUniform("lightColor");
	}
	
	public void setAbientLighting(Color color) {
		GL33.glUniform4f(ambientColorUniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0f);
	}
	public void setLightPosition(Vector3D pos) {
		GL33.glUniform3f(lightPosUniform, (float)pos.x, (float)pos.y, (float)pos.z);
	}
	public void setLightColor(Color color) {
		GL33.glUniform4f(lightColorUniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0f);
	}
	
}
