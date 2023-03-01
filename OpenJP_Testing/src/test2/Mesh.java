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
		indexLenght = vertices.length;
	}
	public Mesh(float[] vertices, int[] indicies) {
		vao = new VAO();
		
		vao.storeVertexIndexData( vertices, indicies);
		indexLenght = indicies.length;
		indexMode = true;
	}
	public Mesh(float[] vertices, boolean combined) {
		
		float[] vert = new float[vertices.length/3];
		float[] color = new float[vertices.length/3];
		float[] norm = new float[vertices.length/3];
		
		for(int i = 0; i < vert.length/3; i++) {
			int vI = (i*3)*3;
			vert[i*3] = vertices[vI];
			vert[i*3+1] = vertices[vI+1];
			vert[i*3+2] = vertices[vI+2];
			int cI = (i*3 + 1)*3;
			color[i*3] = vertices[cI];
			color[i*3+1] = vertices[cI+1];
			color[i*3+2] = vertices[cI+2];
			int nI = (i*3 + 2)*3;
			color[i*3] = vertices[nI];
			color[i*3+1] = vertices[nI+1];
			color[i*3+2] = vertices[nI+2];
		}

		vao = new VAO();
		
		indexLenght = vert.length;
		
		vao.storeDataInAttributeList(0, vert);
		vao.storeColorData(color);
		vao.storeNormalData(norm);
	}
	
	public void setColors(float[] colors) {
		vao.storeColorData(colors);
	}
	public void setNormals(float[] normals) {
		vao.storeNormalData(normals);
	}
	
	public void render() {
		vao.bind();
		
		if(indexMode) {
			GL33.glDrawElements(GL33.GL_TRIANGLES, indexLenght, GL33.GL_UNSIGNED_INT, 0);
		} else {
			GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, indexLenght);
		}
	}
	
	public void dispose() {
		vao.dispose();
	}
}
