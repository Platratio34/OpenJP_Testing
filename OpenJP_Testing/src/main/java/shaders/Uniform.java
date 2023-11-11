package shaders;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.ARBBindlessTexture;
import org.lwjgl.opengl.GL44;
import org.lwjgl.system.MemoryStack;

import objects.Texture2D;

public class Uniform {

	private int uniformId;
	public Uniform(ShaderProgram shader, String name) {
		uniformId = shader.getUniform(name);
	}
	
	public void setMatrix4f(Matrix4f matrix) { setMatrix4f(uniformId, matrix); }
	public static void setMatrix4f(int uniform, Matrix4f matrix) {
		if(uniform < 0) return;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			matrix.get(fb);
			GL44.glUniformMatrix4fv(uniform, false, fb);
		}
	}
	
	public void setColor(Color color) { setColor(uniformId, color); }
	public static void setColor(int uniform, Color color) {
		if(uniform < 0) return;
		GL44.glUniform3f(uniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
	}
	public void setColor4(Color color) { setColor4(uniformId, color); }
	public static void setColor4(int uniform, Color color) {
		if(uniform < 0) return;
		GL44.glUniform4f(uniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
	}
	
	public void setVector3f(Vector3f vector) { setVector3f(uniformId, vector); }
	public static void setVector3f(int uniform, Vector3f vector) {
		if(uniform < 0) return;
		GL44.glUniform3f(uniform, vector.x, vector.y, vector.z);
	}
	
	public void setColorArray(Color[] array) { setColorArray(uniformId, array); }
	public static void setColorArray(int uniform, Color[] array) {
		if(uniform < 0) return;
		for(int i = 0; i < array.length; i++) {
			setColor(uniform + i, array[i]);
		}
	}
	
	public void setBoolean(boolean bool) { setBoolean(uniformId, bool); }
	public static void setBoolean(int uniform, boolean bool) {
		if(uniform < 0) return;
		GL44.glUniform1i(uniform, bool?1:0);
	}
	
	public void setFloat(float val) { setFloat(uniformId, val); }
	public static void setFloat(int uniform, float val) {
		if(uniform < 0) return;
		GL44.glUniform1f(uniform, val);
	}

	public void setTexture2D(Texture2D texture) { setTexture2D(uniformId, texture); }
	public static void setTexture2D(int uniform, Texture2D texture) {
		if (uniform < 0)
			return;
		texture.makeResident();
		ARBBindlessTexture.glUniformHandleui64ARB(uniform, texture.getHandle());
	}
}
