package test2;

import java.awt.Color;

import org.joml.Vector3f;
import test.ShaderProgram;

public class LightingSettings {

	private int ambientColorUniform;
	private int lightPosUniform;
	private int lightColorUniform;
	
	public LightingSettings(ShaderProgram shader) {
		ambientColorUniform = shader.getUniform("ambientColor");
		lightPosUniform = shader.getUniform("lightPos");
		lightColorUniform = shader.getUniform("lightColor");
//		System.out.println("aC"+ambientColorUniform+" lp"+lightPosUniform+" lC"+lightColorUniform);
	}
	
	public void setAbientLighting(Color color) {
//		GL33.glUniform3f(ambientColorUniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		Uniform.setColor(ambientColorUniform, color);
	}
	public void setLightPosition(Vector3f pos) {
//		GL33.glUniform3f(lightPosUniform, pos.x, pos.y, pos.z);
		Uniform.setVector3f(lightPosUniform, pos);
	}
	public void setLightColor(Color color) {
//		GL33.glUniform3f(lightColorUniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		Uniform.setColor(lightColorUniform, color);
	}
	
}
