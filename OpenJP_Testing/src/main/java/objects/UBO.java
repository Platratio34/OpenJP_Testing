package objects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL44;
import org.lwjgl.system.MemoryStack;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

/**
 * Generic Uniform Buffer Object
 */
public class UBO {
	
	private int bufferId;
	private long words;
	private int binding;

	/** Length of a word in bytes */
	public static final long WORD_LENGTH = 4l;

	/** Length of a float in std140 in words */
	public static final long FLOAT_SIZE = 1l; 
	/** Length of a integer in std140 in words */
	public static final long INT_SIZE = 1l; 
	/** Length of a Vector2f in std140 in words */
	public static final long VEC2_SIZE = FLOAT_SIZE * 2;
	/** Length of a Vector3f in std140 in words */
	public static final long VEC3_SIZE = FLOAT_SIZE * 4;
	/** Length of a Vector4f in std140 in words */
	public static final long VEC4_SIZE = FLOAT_SIZE * 4;
	/** Length of a Matrix4f in std140 in words */
	public static final long MAT4_SIZE = FLOAT_SIZE * 4 * 4;

	/** Length of a sampler2D reference in std140 in words */
	public static final long SAMPLER_2D_SIZE = 2;
	
	/**
	 * Create a Uniform Buffer Object.<br>
	 * <br>
	 * Binds UBO to specified target<br><br>
	 * Use <code>UBO.*_SIZE</code> for data type sizes in <code>std140</code>
	 * @param words length of the buffer in words (see <code>UBO.WORD_LENGTH</code> for word length)
	 * @param target binding target for the UBO
	 */
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

	/**
	 * Get the OpenGL id of the buffer
	 * @return buffer id
	 */
	public int getBufferId() {
		return bufferId;
	}
	
	/**
	 * Set a section of the buffer starting at <code>startWord</code>
	 * @param startWord word to start writing at
	 * @param buffer data to write
	 */
	public void set(long startWord, ByteBuffer buffer) {
		if (startWord >= words) {
			System.err.println("Tried to set outside of UBO; length=" + words + ", start=" + startWord);
			return;
		}
		bind();
		GL44.glBufferSubData(GL44.GL_UNIFORM_BUFFER, startWord * WORD_LENGTH, buffer);
		unbind();
	}

	/**
	 * Set a section of the buffer starting at <code>startWord</code>
	 * @param startWord word to start writing at
	 * @param buffer data to write
	 */
	public void set(long startWord, IntBuffer buffer) {
		if (startWord >= words) {
			System.err.println("Tried to set outside of UBO; length=" + words + ", start=" + startWord);
			return;
		}
		bind();
		GL44.glBufferSubData(GL44.GL_UNIFORM_BUFFER, startWord * WORD_LENGTH, buffer);
		unbind();
	}
	
	/**
	 * Set a section of the buffer starting at <code>startWord</code> to a integer
	 * @param startWord word to start writing at
	 * @param dat integer to set
	 */
	public void set(long startWord, int data) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer ib = stack.mallocInt(1);
			ib.put(data);
			ib.rewind();
			set(0, ib);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set a section of the buffer starting at <code>startWord</code> to a boolean
	 * @param startWord word to start writing at
	 * @param dat boolean to set
	 */
	public void set(long startWord, boolean data) {
		set(startWord, data ? 1 : 0);
	}

	/**
	 * Set a section of the buffer starting at <code>startWord</code>
	 * @param startWord word to start writing at
	 * @param buffer data to write
	 */
	public void set(long startWord, LongBuffer buffer) {
		if (startWord >= words) {
			System.err.println("Tried to set outside of UBO; length=" + words + ", start=" + startWord);
			return;
		}
		bind();
		GL44.glBufferSubData(GL44.GL_UNIFORM_BUFFER, startWord * WORD_LENGTH, buffer);
		unbind();
	}
	
	/**
	 * Set a section of the buffer starting at <code>startWord</code> to a long
	 * @param startWord word to start writing at
	 * @param dat long to set
	 */
	public void set(long startWord, long data) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			LongBuffer lb = stack.mallocLong(1);
			lb.put(data);
			lb.rewind();
			set(0, lb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set a section of the buffer starting at <code>startWord</code>
	 * @param startWord word to start writing at
	 * @param buffer data to write
	 */
	public void set(long startWord, FloatBuffer buffer) {
		if (startWord >= words) {
			System.err.println("Tried to set outside of UBO; length=" + words + ", start=" + startWord);
			return;
		}
		bind();
		GL44.glBufferSubData(GL44.GL_UNIFORM_BUFFER, startWord * WORD_LENGTH, buffer);
		unbind();
	}

	/**
	 * Set a section of the buffer starting at <code>startWord</code> to a color (including alpha)
	 * @param startWord word to start writing at
	 * @param color color to set
	 */
	public void set(long startWord, Color color) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(4);
			fb.put(color.getRed() / 255f);
			fb.put(color.getGreen() / 255f);
			fb.put(color.getBlue() / 255f);
			fb.put(color.getAlpha() / 255f);
			fb.rewind();
			set(0, fb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set a section of the buffer starting at <code>startWord</code> to a float
	 * @param startWord word to start writing at
	 * @param dat float to set
	 */
	public void set(long startWord, float data) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(1);
			fb.put(data);
			fb.rewind();
			set(0, fb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set a section of the buffer starting at <code>startWord</code> to a vector 2
	 * @param startWord word to start writing at
	 * @param vector vector to set
	 */
	public void set(long startWord, Vector2f vector) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(2);
			vector.get(fb);
			fb.rewind();
			set(0, fb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set a section of the buffer starting at <code>startWord</code> to a vector 3
	 * @param startWord word to start writing at
	 * @param vector vector to set
	 */
	public void set(long startWord, Vector3f vector) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(3);
			vector.get(fb);
			fb.rewind();
			set(0, fb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Bind the buffer for writing
	 */
	public void bind() {
		GL44.glBindBuffer(GL44.GL_UNIFORM_BUFFER, bufferId);
	}
	/**
	 * Unbind the buffer after writing
	 */
	public void unbind() {
		GL44.glBindBuffer(GL44.GL_UNIFORM_BUFFER, 0);
	}
	/**
	 * Bind the buffer to the target binding
	 */
	public void bindTarget() {
		bindTarget(binding);
	}
	/**
	 * Bind the buffer to an alternant binding
	 * @param binding binding index
	 */
	public void bindTarget(int binding) {
		GL44.glBindBufferRange(GL44.GL_UNIFORM_BUFFER, binding, bufferId, 0, words * WORD_LENGTH);
	}

	/**
	 * Attempt to get the currently bound UBO for writing
	 * @return UBO ID, may be inaccurate
	 */
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
