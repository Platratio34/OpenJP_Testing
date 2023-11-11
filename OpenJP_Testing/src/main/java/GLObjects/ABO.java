package GLObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL44;

public class ABO {
	
	private int id;
	private int type;
	
	public ABO(int type) {
		this.type = type;
		id = GL44.glGenBuffers();
	}
	
	public void fill(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bind();
		GL44.glBufferData(type, buffer, GL44.GL_STATIC_DRAW);
//		MemoryUtil.memFree(buffer);
	}
	public void fill(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bind();
		GL44.glBufferData(type, buffer, GL44.GL_STATIC_DRAW);
//		MemoryUtil.memFree(buffer);
	}
	
	public void bind() {
		GL44.glBindBuffer(type, id);
	}
	public void unbind() {
		GL44.glBindBuffer(type, 0);
	}
	
	public void dispose() {
		GL44.glDeleteBuffers(id);
	}
}
