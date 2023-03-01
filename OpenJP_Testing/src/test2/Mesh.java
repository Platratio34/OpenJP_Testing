package test2;

import org.lwjgl.opengl.GL33;

import objects.VAO;

public class Mesh {

	
	private VAO vao;
	private int indexLenght;
	private boolean indexMode = false;
	
	public Mesh(float[] vertices) {
		vao = new VAO();
		
		vao.storeDataInAttributeList(0, vertices);
	}
	public Mesh(float[] vertices, int[] indicies) {
		vao = new VAO();
		
		vao.storeVertexIndexData( vertices, indicies);
		indexLenght = indicies.length;
		indexMode = true;
	}
	
	public void setColors(float[] colors) {
		vao.storeColorData(colors);
	}
	public void setNormals(float[] normals) {
		vao.storeNormalData(normals);
	}
	
	public void render() {
		vao.bind();
//		ebo.bind();
		
		if(indexMode) {
			GL33.glDrawElements(GL33.GL_TRIANGLES, indexLenght, GL33.GL_UNSIGNED_INT, 0);
		} else {
			GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 3);
		}
		
//		VAO.unbind();
	}
	
	public void dispose() {
		vao.dispose();
	}
}
