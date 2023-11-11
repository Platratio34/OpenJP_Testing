package objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import org.lwjgl.opengl.GL44;

import GLObjects.VAO;
import oldObjects.Triangle;
import java.io.InputStreamReader;

public class Mesh {

	
	private VAO vao;
	private int indexLength;
	private boolean indexMode = false;
	
	public Mesh() {
		vao = new VAO();
	}

	public Mesh(float[] vertices) {
		vao = new VAO();
		
		vao.storeDataInAttributeList(0, vertices);
		indexLength = vertices.length;
	}

	public Mesh(float[] vertices, int[] indices) {
		vao = new VAO();

		vao.storeVertexIndexData(vertices, indices);
		indexLength = indices.length;
		indexMode = true;
	}
	
	public void setVertices(float[] vertices) {
		vao.storeDataInAttributeList(0, vertices);
		indexLength = vertices.length;
	}
	public void setColors(float[] colors) {
		vao.storeColorData(colors);
	}
	public void setNormals(float[] normals) {
		vao.storeNormalData(normals);
	}
	public void setUVs(float[] uvs) {
		vao.storeUVData(uvs);
	}
	
	public void render() {
		vao.bind();
		GL44.glEnableVertexAttribArray(0);
		GL44.glEnableVertexAttribArray(1);
		GL44.glEnableVertexAttribArray(3);
		
		if(indexMode) {
			GL44.glDrawElements(GL44.GL_TRIANGLES, indexLength, GL44.GL_UNSIGNED_INT, 4);
		} else {
			GL44.glDrawArrays(GL44.GL_TRIANGLES, 0, indexLength);
		}
		
		GL44.glDisableVertexAttribArray(0);
		GL44.glDisableVertexAttribArray(1);
		GL44.glDisableVertexAttribArray(3);
		VAO.unbind();
	}
	
	public void dispose() {
		vao.dispose();
	}
	
	public static Mesh createFromSingleArray(float[] array) {
		float[] vert = new float[array.length / 3];
		float[] color = new float[array.length / 3];
		float[] norm = new float[array.length / 3];

		for (int i = 0; i < vert.length / 3; i++) {
			int vI = (i * 3) * 3;
			vert[i * 3] = array[vI];
			vert[i * 3 + 1] = array[vI + 1];
			vert[i * 3 + 2] = array[vI + 2];
			int cI = (i * 3 + 1) * 3;
			color[i * 3] = array[cI];
			color[i * 3 + 1] = array[cI + 1];
			color[i * 3 + 2] = array[cI + 2];
			int nI = (i * 3 + 2) * 3;
			norm[i * 3] = array[nI];
			norm[i * 3 + 1] = array[nI + 1];
			norm[i * 3 + 2] = array[nI + 2];
		}
		Mesh mesh = new Mesh(vert);
		mesh.setColors(color);
		mesh.setNormals(norm);
		return mesh;
	}

	public static Mesh createFromString(String str) {
		return createFromString(str, new Mesh());
	}
	public static Mesh createFromString(String str, Mesh mesh) {
		String[] components = str.split(";");
		int sI = 0;
		int vI = 0;
		int cI = 1;
		int nI = 2;
		int mI = -1;
		
		if(components[0].contains("format=")) {
			sI++;
			String[] parts = components[0].substring(8).split(",");
			// vI = -1;
			cI = -1;
			nI = -1;
			for(int i = 0; i < parts.length; i++) {
				if(parts[i].equals("vertex")) {
					vI = i;
				} else if(parts[i].equals("color")) {
					cI = i;
				} else if(parts[i].equals("normal")) {
					nI = i;
				} else if(parts[i].equals("mat")) {
					mI = i;
				}
			}
		}
		
		int vlength = components.length - sI;
		float[] verts = new float[vlength];
		float[] colors = new float[vlength];
		float[] normals = new float[vlength];
		
		int type = 0;
		int arrI = 0;
		for(int i = sI; i < components.length; i++) {
			String[] parts = components[i].split(",");
			if(type == vI) {
				verts[arrI] = Float.parseFloat(parts[0]);
				verts[arrI+1] = Float.parseFloat(parts[1]);
				verts[arrI+2] = Float.parseFloat(parts[2]);
//				System.out.print(String.format("v=%f,%f,%f; ", verts[arrI], verts[arrI+1], verts[arrI+2]));
			} else if(type == cI) {
				colors[arrI] = Float.parseFloat(parts[0]);
				colors[arrI+1] = Float.parseFloat(parts[1]);
				colors[arrI+2] = Float.parseFloat(parts[2]);
//				System.out.print(String.format("c=%f,%f,%f; ", colors[arrI], colors[arrI+1], colors[arrI+2]));
			} else if(type == nI) {
				normals[arrI] = Float.parseFloat(parts[0]);
				normals[arrI+1] = Float.parseFloat(parts[1]);
				normals[arrI+2] = Float.parseFloat(parts[2]);
//				System.out.print(String.format("n=%f,%f,%f; ", normals[arrI], normals[arrI+1], normals[arrI+2]));
			} else if(type == mI) {
				colors[arrI] = Float.parseFloat(parts[0]);
			}
			
			type = (type+1)%3;
			if(type == 0) {
				arrI += 3;
//				System.out.println("");
			}
		}

		mesh.setVertices(verts);
		mesh.setColors(colors);
		mesh.setNormals(normals);
		return mesh;
	}

	public static Mesh createFromFile(String path) throws IOException {
		return createFromFile(path, new Mesh());
	}

	public static Mesh createFromFile(String path, Mesh mesh) throws IOException {
		File f = new File(path);
		System.out.println(f.getAbsolutePath());
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		String str = "";
		while (line != null) {
			str += line;
			line = br.readLine();
		}
		br.close();
		return createFromString(str, mesh);
	}
	public static Mesh createFromResource(String resource) throws IOException {
		// BufferedReader br = new BufferedReader(new FileReader(path));
		ClassLoader classLoader = Triangle.class.getClassLoader();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resource)));
		} catch (NullPointerException e) {
			System.err.println("Could not load mesh from resource \""+resource+"\"");
			return null;
		}
		String line = "";
		String str = "";
		while (line != null) {
			str += line;
			line = br.readLine();
		}
		br.close();
		return createFromString(str);
	}
}
