package objects;

import org.lwjgl.opengl.GL44;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class UBO {
	
	private int bufferId;
	private long words;
	private int binding;

	/** Length of a word in bytes */
	public static final long WORD_LENGTH = 4l;

	/** Length of a float in std140 in words */
	public static final long FLOAT_SIZE = 1l; 
	/** Length of a Vector3f in std140 in words */
	public static final long VEC3_SIZE = FLOAT_SIZE * 4;
	/** Length of a Vector4f in std140 in words */
	public static final long VEC4_SIZE = FLOAT_SIZE * 4;
	/** Length of a Matrix4f in std140 in words */
	public static final long MAT4_SIZE = FLOAT_SIZE * 4 * 4;
	
	public UBO(long words, int target) {
		this.words = words;
		this.binding = target;
		bufferId = GL44.glGenBuffers();
		bind();
		GL44.glBufferData(GL44.GL_UNIFORM_BUFFER, words * WORD_LENGTH, GL44.GL_DYNAMIC_DRAW);
		unbind();
		bindTarget();
		// System.out.println("Created UBO @ buffer "+bufferId+" for binding "+binding+" with length "+words+" words");
	}

	public int getBufferId() {
		return bufferId;
	}
	
	public void set(long startWord, ByteBuffer buffer) {
		if(startWord >= words) {
			System.err.println("Tried to set outside of UBO; length="+words+", start="+startWord);
			return;
		}
		bind();
		GL44.glBufferSubData(GL44.GL_UNIFORM_BUFFER, startWord*WORD_LENGTH, buffer);
		unbind();
	}
	public void set(long startWord, FloatBuffer buffer) {
		if(startWord >= words) {
			System.err.println("Tried to set outside of UBO; length="+words+", start="+startWord);
			return;
		}
		bind();
		GL44.glBufferSubData(GL44.GL_UNIFORM_BUFFER, startWord*WORD_LENGTH, buffer);
		unbind();
	}
	
	
	public void bind() {
		GL44.glBindBuffer(GL44.GL_UNIFORM_BUFFER, bufferId);
	}
	public void bindTarget() {
		bindTarget(binding);
	}
	public void bindTarget(int binding) {
		GL44.glBindBufferRange(GL44.GL_UNIFORM_BUFFER, binding, bufferId, 0, words * WORD_LENGTH);
	}
	public void unbind() {
		GL44.glBindBuffer(GL44.GL_UNIFORM_BUFFER, 0);
	}

	public static int getCurrentUBOBound() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer buffer = stack.mallocInt(1);
			GL44.glGetIntegerv(GL44.GL_UNIFORM_BUFFER, buffer);
			return buffer.get(0);
		} catch(Exception e) {
            e.printStackTrace();
        }
		return -1;
	}
}
