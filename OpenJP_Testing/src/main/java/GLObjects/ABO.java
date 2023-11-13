package GLObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL44;

/**
 * Array Buffer Object<br>
 * <br>
 * OpenGL wrapper
 */
public class ABO {
	
	private int id;
	private int type;
	
	/**
	 * Create a new ABO
	 * @param type buffer type
	 */
	public ABO(int type) {
		this.type = type;
		id = GL44.glGenBuffers();
	}
	
	/**
	 * Fill the buffer with data
	 * @param data new buffer data
	 */
	public void fill(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bind();
		GL44.glBufferData(type, buffer, GL44.GL_STATIC_DRAW);
//		MemoryUtil.memFree(buffer);
	}
	/**
	 * Fill the buffer with data
	 * @param data new buffer data
	 */
	public void fill(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bind();
		GL44.glBufferData(type, buffer, GL44.GL_STATIC_DRAW);
		//		MemoryUtil.memFree(buffer);
	}
	
	/**
	 * Bind the buffer for modification
	 */
	public void bind() {
		GL44.glBindBuffer(type, id);
	}

	/**
	 * Unbind buffer from modification
	 */
	public void unbind() {
		GL44.glBindBuffer(type, 0);
	}
	
	/**
	 * Dispose of the buffer clearing graphics memory
	 */
	public void dispose() {
		GL44.glDeleteBuffers(id);
	}
}
