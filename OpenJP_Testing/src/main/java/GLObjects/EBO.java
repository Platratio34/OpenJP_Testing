package GLObjects;

import org.lwjgl.opengl.GL30;

public class EBO {

	private int id;
	private int length;
	
	public EBO(int[] indicies) {
		id = GL30.glGenBuffers();
		storeIndexData(indicies);
	}
	
	public void storeIndexData(int[] indicies) {
		bind();
		
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indicies, GL30.GL_STATIC_DRAW);
		length = indicies.length;
	}
	
	public void bind() {
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, id);
	}
	public static void unbind() {
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void dispose() {
		GL30.glDeleteBuffers(id);
	}

	public int getLength() {
		return length;
	}
}
