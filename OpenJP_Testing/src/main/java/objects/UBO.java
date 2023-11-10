package objects;

import org.lwjgl.opengl.GL33;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class UBO {
	
	private int id;
	private long words;

	public static final long WORD_LENGTH = 4l;
	
	public static final long FLOAT_SIZE = 1l;
	public static final long VEC4_SIZE = FLOAT_SIZE * 4;
	public static final long MAT4_SIZE = FLOAT_SIZE * 4 * 4;
	
	public UBO(long words, int target) {
		this.words = words;
		id = GL33.glGenBuffers();
		bind();
		GL33.glBufferData(GL33.GL_UNIFORM_BUFFER, words * WORD_LENGTH, GL33.GL_DYNAMIC_DRAW);
		unbind();
		GL33.glBindBufferBase(GL33.GL_UNIFORM_BUFFER, target, id);
	}
	
	public void set(long startWord, ByteBuffer buffer) {
		if(startWord > words) {
			System.err.println("Tried to set outside of UBO; length="+words+", start="+startWord);
			return;
		}
		bind();
		GL33.glBufferSubData(id, startWord*WORD_LENGTH, buffer);
		unbind();
	}
	public void set(long startWord, FloatBuffer buffer) {
		if(startWord > words) {
			System.err.println("Tried to set outside of UBO; length="+words+", start="+startWord);
			return;
		}
		bind();
		GL33.glBufferSubData(id, startWord*WORD_LENGTH, buffer);
		unbind();
	}
	
	public void bind() {
		GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, id);
		// System.out.println("UBO bound");
	}
	public void unbind() {
		GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, 0);
	}
}
