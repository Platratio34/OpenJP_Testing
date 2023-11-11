package GLObjects;

import java.util.ArrayList;

import org.lwjgl.opengl.GL30;

public class VAO {
	
	private int id;
	
	private ArrayList<ABO> abos;
	
	public VAO() {
		id = GL30.glGenVertexArrays();
		abos = new ArrayList<ABO>();
	}
	
	public void storeDataInAttributeList(int attrib, float[] data) {
		storeDataInAttributeList(attrib, data, 3);
	}

	public void storeDataInAttributeList(int attrib, float[] data, int size) {
		bind();
		ABO abo = new ABO(GL30.GL_ARRAY_BUFFER);
		abo.fill(data);
		abos.add(abo);
		GL30.glEnableVertexAttribArray(attrib);
		GL30.glVertexAttribPointer(attrib, size, GL30.GL_FLOAT, false, 0, 0);
		abo.unbind();
	}
	
	public void storeVertexIndexData(float[] verticies, int[] indicies) {
		bind();
		ABO vbo = new ABO(GL30.GL_ARRAY_BUFFER);
		vbo.fill(verticies);
		abos.add(vbo);
		GL30.glEnableVertexAttribArray(0);
		GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
		ABO ebo = new ABO(GL30.GL_ELEMENT_ARRAY_BUFFER);
		ebo.fill(indicies);
		abos.add(ebo);
		GL30.glEnableVertexAttribArray(4);
		GL30.glVertexAttribPointer(4, 2, GL30.GL_UNSIGNED_INT, false, 0, 0);
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
	// 	ABO abo = new ABO(GL30.GL_ARRAY_BUFFER);
	// 	abo.fill(materials);
	// 	abos.add(abo);
	// 	GL30.glEnableVertexAttribArray(3);
	// 	GL30.glVertexAttribPointer(3, 3, GL30.GL_INT, false, 0, 0);
	// 	abo.unbind();
	// }
	
	public void bind() {
		GL30.glBindVertexArray(id);
	}
	public static void unbind() {
		GL30.glBindVertexArray(0);
	}
	
	public void dispose() {
		GL30.glDeleteVertexArrays(id);
		for (ABO abo : abos) {
			abo.dispose();
		}
	}
	
	public int getId() {
		return id;
	}
}
