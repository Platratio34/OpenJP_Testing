package GLObjects;

import org.lwjgl.opengl.GL44;

/**
 * Element Buffer Object<br>
 * <br>
 * OpenGL wrapper
 */
public class EBO {

	private int id;
	private int length;
	
	/**
	 * Create a new EBO filled with index data
	 * @param indices flattened index data
	 */
	public EBO(int[] indices) {
		id = GL44.glGenBuffers();
		storeIndexData(indices);
	}
	
	/**
	 * Set index data
	 * @param indices flattened index data
	 */
	public void storeIndexData(int[] indices) {
		bind();

		GL44.glBufferData(GL44.GL_ELEMENT_ARRAY_BUFFER, indices, GL44.GL_STATIC_DRAW);
		length = indices.length;
	}
	
	/**
	 * Bind buffer for usage
	 */
	public void bind() {
		GL44.glBindBuffer(GL44.GL_ELEMENT_ARRAY_BUFFER, id);
	}

	/**
	 * Unbind the buffer
	 */
	public static void unbind() {
		GL44.glBindBuffer(GL44.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Dispose of the buffer clearing graphics memory
	 */
	public void dispose() {
		GL44.glDeleteBuffers(id);
	}

	/**
	 * Get the length of the indices stored in the buffer
	 * @return index length
	 */
	public int getLength() {
		return length;
	}
}
