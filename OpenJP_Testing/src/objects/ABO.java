package objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class ABO {
	
	private int id;
	private int type;
	
	public ABO(int type) {
		this.type = type;
		id = GL30.glGenBuffers();
	}
	
	public void fill(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bind();
		GL30.glBufferData(type, buffer, GL30.GL_STATIC_DRAW);
//		MemoryUtil.memFree(buffer);
	}
	public void fill(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bind();
		GL30.glBufferData(type, buffer, GL30.GL_STATIC_DRAW);
//		MemoryUtil.memFree(buffer);
	}
	
	public void bind() {
		GL30.glBindBuffer(type, id);
	}
	public void unbind() {
		GL30.glBindBuffer(type, 0);
	}
	
	public void dispose() {
		GL30.glDeleteBuffers(id);
	}
}
