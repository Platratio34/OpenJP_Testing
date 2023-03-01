package objects;

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
		bind();
		ABO abo = new ABO(GL30.GL_ARRAY_BUFFER);
		abo.fill(data);
		abos.add(abo);
		GL30.glVertexAttribPointer(attrib, 3, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(attrib);
		abo.unbind();
	}
	
	public void storeVertexIndexData(float[] verticies, int[] indicies) {
		bind();
		ABO vbo = new ABO(GL30.GL_ARRAY_BUFFER);
		vbo.fill(verticies);
		abos.add(vbo);
		ABO ebo = new ABO(GL30.GL_ELEMENT_ARRAY_BUFFER);
		ebo.fill(indicies);
		abos.add(ebo);
		GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(0);
		vbo.unbind();
	}
	
	public void storeColorData(float[] colors) {
		storeDataInAttributeList(1, colors);
//		bind();
//		ABO vbo = new ABO(GL30.GL_ARRAY_BUFFER);
//		vbo.fill(colors);
//		abos.add(vbo);
//		GL30.glVertexAttribPointer(1, 3, GL30.GL_FLOAT, false, 0, 0);
//		GL30.glEnableVertexAttribArray(1);
//		vbo.unbind();
	}
	public void storeNormalData(float[] normals) {
		storeDataInAttributeList(2, normals);
	}
	
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
