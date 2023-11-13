package shaders;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.ARBBindlessTexture;
import org.lwjgl.opengl.GL44;
import org.lwjgl.system.MemoryStack;

import objects.Texture2D;

/**
 * Uniform utility and reference class
 */
public class Uniform {

	private int uniformId;
	private ShaderProgram shader;

	/**
	 * Create a uniform reference
	 * @param shader shader program the uniform is in
	 * @param name uniform name
	 */
	public Uniform(ShaderProgram shader, String name) {
		uniformId = shader.getUniform(name);
		this.shader = shader;
	}
	
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param matrix value
	 */
	public void setMatrix4f(Matrix4f matrix) { shader.bind(); setMatrix4f(uniformId, matrix); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param matrix value
	 */
	public static void setMatrix4f(int uniform, Matrix4f matrix) {
		if(uniform < 0) return;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			matrix.get(fb);
			GL44.glUniformMatrix4fv(uniform, false, fb);
		}
	}
	
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param color value
	 */
	public void setColor(Color color) { shader.bind(); setColor(uniformId, color); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param color value
	 */
	public static void setColor(int uniform, Color color) {
		if(uniform < 0) return;
		GL44.glUniform3f(uniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
	}
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param color value
	 */
	public void setColor4(Color color) { shader.bind(); setColor4(uniformId, color); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param color value
	 */
	public static void setColor4(int uniform, Color color) {
		if(uniform < 0) return;
		GL44.glUniform4f(uniform, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
	}
	
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param vector value
	 */
	public void setVector3f(Vector3f vector) { shader.bind(); setVector3f(uniformId, vector); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param vector value
	 */
	public static void setVector3f(int uniform, Vector3f vector) {
		if (uniform < 0)
			return;
		GL44.glUniform3f(uniform, vector.x, vector.y, vector.z);
	}
	
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param vector value
	 */
	public void setVector2f(Vector2f vector) { shader.bind(); setVector2f(uniformId, vector); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param vector value
	 */
	public static void setVector2f(int uniform, Vector2f vector) {
		if(uniform < 0) return;
		GL44.glUniform2f(uniform, vector.x, vector.y);
	}
	
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param array value
	 */
	public void setColorArray(Color[] array) { shader.bind(); setColorArray(uniformId, array); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param array value
	 */
	public static void setColorArray(int uniform, Color[] array) {
		if(uniform < 0) return;
		for(int i = 0; i < array.length; i++) {
			setColor(uniform + i, array[i]);
		}
	}
	
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param bool value
	 */
	public void setBoolean(boolean bool) { shader.bind(); setBoolean(uniformId, bool); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param bool value
	 */
	public static void setBoolean(int uniform, boolean bool) {
		if(uniform < 0) return;
		GL44.glUniform1i(uniform, bool?1:0);
	}
	
	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param val value
	 */
	public void setFloat(float val) { shader.bind(); setFloat(uniformId, val); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param val value
	 */
	public static void setFloat(int uniform, float val) {
		if(uniform < 0) return;
		GL44.glUniform1f(uniform, val);
	}

	/**
	 * Set the uniform value.<br>
     * <br>
     * <b>Binds shader program</b>
	 * @param texture value
	 */
	public void setTexture2D(Texture2D texture) { shader.bind(); setTexture2D(uniformId, texture); }
	/**
	 * Set uniform value by ID.<br>
	 * <br>
	 * <b>Shader must be bound before setting</b>
	 * @param uniform uniform ID
	 * @param texture value
	 */
	public static void setTexture2D(int uniform, Texture2D texture) {
		if (uniform < 0)
			return;
		texture.makeResident();
		ARBBindlessTexture.glUniformHandleui64ARB(uniform, texture.getHandle());
	}
}
