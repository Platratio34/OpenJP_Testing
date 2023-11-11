package GLObjects;

import java.util.ArrayList;

import org.lwjgl.opengl.GL44;

public class VAO {
	
	private int id;
	
	private ArrayList<ABO> abos;
	
	public VAO() {
		id = GL44.glGenVertexArrays();
		abos = new ArrayList<ABO>();
	}
	
	public void storeDataInAttributeList(int attrib, float[] data) {
		storeDataInAttributeList(attrib, data, 3);
	}

	public void storeDataInAttributeList(int attrib, float[] data, int size) {
		bind();
		ABO abo = new ABO(GL44.GL_ARRAY_BUFFER);
		abo.fill(data);
		abos.add(abo);
		GL44.glEnableVertexAttribArray(attrib);
		GL44.glVertexAttribPointer(attrib, size, GL44.GL_FLOAT, false, 0, 0);
		abo.unbind();
	}
	
	public void storeVertexIndexData(float[] verticies, int[] indicies) {
		bind();
		ABO vbo = new ABO(GL44.GL_ARRAY_BUFFER);
		vbo.fill(verticies);
		abos.add(vbo);
		GL44.glEnableVertexAttribArray(0);
		GL44.glVertexAttribPointer(0, 3, GL44.GL_FLOAT, false, 0, 0);
		ABO ebo = new ABO(GL44.GL_ELEMENT_ARRAY_BUFFER);
		ebo.fill(indicies);
		abos.add(ebo);
		GL44.glEnableVertexAttribArray(4);
		GL44.glVertexAttribPointer(4, 2, GL44.GL_UNSIGNED_INT, false, 0, 0);
		vbo.unbind();
	}
	
	public void storeColorData(float[] colors) {
		storeDataInAttributeList(1, colors, 3);
	}
	public void storeNormalData(float[] normals) {
		storeDataInAttributeList(2, normals, 3);
	}
	public void storeUVData(float[] uvs) {
		storeDataInAttributeList(3, uvs, 2);
	}
	// public void storeMatData(int[] materials) {
	// 	bind();
	// 	ABO abo = new ABO(GL44.GL_ARRAY_BUFFER);
	// 	abo.fill(materials);
	// 	abos.add(abo);
	// 	GL44.glEnableVertexAttribArray(3);
	// 	GL44.glVertexAttribPointer(3, 3, GL44.GL_INT, false, 0, 0);
	// 	abo.unbind();
	// }
	
	public void bind() {
		GL44.glBindVertexArray(id);
	}
	public static void unbind() {
		GL44.glBindVertexArray(0);
	}
	
	public void dispose() {
		GL44.glDeleteVertexArrays(id);
		for (ABO abo : abos) {
			abo.dispose();
		}
	}
	
	public int getId() {
		return id;
	}
}
