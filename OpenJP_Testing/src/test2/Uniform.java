package test2;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public class Uniform {
	
	public static void setMatrix4f(int uniform, Matrix4f matrix) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			matrix.get(fb);
			GL33.glUniformMatrix4fv(uniform, false, fb);
		}
	}
	
	public static void setColor(int uniform, Color color) {
		GL33.glUniform3f(uniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
	}
	public static void setColor4(int uniform, Color color) {
		GL33.glUniform4f(uniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
	}
	
	public static void setVector3f(int uniform, Vector3f vector) {
		GL33.glUniform3f(uniform, vector.x, vector.y, vector.z);
	}
	
	public static void setColorArray(int uniform, Color[] array) {
		for(int i = 0; i < array.length; i++) {
			setColor(uniform + i, array[i]);
		}
	}
	
	public static void setBoolean(int uniform, boolean bool) {
		GL33.glUniform1i(uniform, bool?1:0);
	}
}
