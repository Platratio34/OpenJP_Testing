package objects;

import org.lwjgl.opengl.GL33;

public class UBO {
	
	private int id;
	
	public UBO() {
		id = GL33.glGenBuffers();
	}
	
	public void set() {
		
	}
	
	public void bind() {
		GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, id);
	}
}
