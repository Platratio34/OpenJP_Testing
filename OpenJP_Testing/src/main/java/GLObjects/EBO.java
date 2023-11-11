package GLObjects;

import org.lwjgl.opengl.GL44;

public class EBO {

	private int id;
	private int length;
	
	public EBO(int[] indicies) {
		id = GL44.glGenBuffers();
		storeIndexData(indicies);
	}
	
	public void storeIndexData(int[] indicies) {
		bind();
		
		GL44.glBufferData(GL44.GL_ELEMENT_ARRAY_BUFFER, indicies, GL44.GL_STATIC_DRAW);
		length = indicies.length;
	}
	
	public void bind() {
		GL44.glBindBuffer(GL44.GL_ELEMENT_ARRAY_BUFFER, id);
	}
	public static void unbind() {
		GL44.glBindBuffer(GL44.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void dispose() {
		GL44.glDeleteBuffers(id);
	}

	public int getLength() {
		return length;
	}
}
