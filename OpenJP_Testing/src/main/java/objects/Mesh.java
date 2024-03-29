package objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import org.lwjgl.opengl.GL44;

import GLObjects.VAO;
import util.MeshData;

import java.io.InputStreamReader;

/**
 * Filled mesh renderer
 */
public class Mesh {

	/** Vertex Array Object for mesh */
	private VAO vao;
	/** Number of vertices or indices depending on mode */
	private int indexLength;
	/** If the mesh should be rendered using indices */
	private boolean indexMode = false;

	/**
	 * Create a new mesh
	 */
	public Mesh() {
		vao = new VAO();
	}

	/**
	 * Create a new mesh in vertex mode
	 * 
	 * @param vertices flattened list of vertices, (ordered x, y, z)
	 */
	public Mesh(float[] vertices) {
		vao = new VAO();

		vao.storeDataInAttributeList(0, vertices);
		indexLength = vertices.length;
	}

	/**
	 * Create a new mesh in index mode
	 * 
	 * @param vertices flattened list of vertices, (ordered x, y, z)
	 * @param indices flattened list of indices, (ordered v1, v2)
	 */
	public Mesh(float[] vertices, int[] indices) {
		vao = new VAO();

		vao.storeVertexIndexData(vertices, indices);
		indexLength = vertices.length;
		indexMode = true;
	}

	/**
	 * Create a new mesh from a MeshData object
	 */
	public Mesh(MeshData data) {
		vao = new VAO();

		set(data);
	}

	/**
	 * Set the vertices of the mesh
	 * 
	 * @param vertices
	 */
	public void setVertices(float[] vertices) {
		vao.storeDataInAttributeList(VAO.ATTRIB_VERTEX, vertices);
		indexLength = vertices.length;
	}

	/**
	 * Set the colors of the mesh
	 */
	public void setColors(float[] colors) {
		vao.storeColorData(colors);
	}

	/**
	 * Set the normals of the mesh
	 * 
	 * @param normals
	 */
	public void setNormals(float[] normals) {
		vao.storeNormalData(normals);
	}

	/**
	 * Set the UVs of the mesh
	 */
	public void setUVs(float[] uvs) {
		vao.storeUVData(uvs);
	}

	/**
	 * Set the values of the mesh from a MeshData
	 */
	public void set(MeshData data) {
		if(data.indices != null) {
			vao.storeVertexIndexData(data.vertices, data.indices);
			indexLength = data.indices.length;
			indexMode = true;
		} else {
			vao.storeVertexData(data.vertices);
			indexLength = data.vertices.length;
		}
		if(data.colors != null)
			vao.storeColorData(data.colors);
		if(data.normals != null)
			vao.storeNormalData(data.normals);
		if(data.uvs != null)
			vao.storeUVData(data.uvs);
	}

	/**
	 * Render the mesh
	 */
	public void render() {
		vao.bind();

		if (indexMode) {
			GL44.glDrawElements(GL44.GL_TRIANGLES, indexLength, GL44.GL_UNSIGNED_INT, 0);
		} else {
			GL44.glDrawArrays(GL44.GL_TRIANGLES, 0, indexLength);
		}

		VAO.unbind();
	}

	/**
	 * Dispose of the mesh
	 */
	public void dispose() {
		vao.dispose();
	}

	/*
	 * Static creation functions
	 */

	/**
	 * Create the mesh from a single combined array
	 */
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

	/**
	 * Create a new mesh from a string
	 * 
	 * @param str mesh string
	 * @return Mesh from the string
	 * 
	 * @deprecated use meshDataFromString and mesh data constructor
	 */
	@Deprecated
	public static Mesh createFromString(String str) {
		return createFromString(str, new Mesh());
	}

	/**
	 * Initialize a mesh from a string
	 * 
	 * @param str  mesh string
	 * @param mesh mesh to initialize
	 * @return Mesh from the string
	 * 
	 * @deprecated use meshDataFromString and mesh data constructor
	 */
	@Deprecated
	public static Mesh createFromString(String str, Mesh mesh) {
		String[] components = str.split(";");
		int sI = 0;
		int vI = 0;
		int cI = 1;
		int nI = 2;
		int mI = -1;

		if (components[0].contains("format=")) {
			sI++;
			String[] parts = components[0].substring(8).split(",");
			// vI = -1;
			cI = -1;
			nI = -1;
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].equals("vertex")) {
					vI = i;
				} else if (parts[i].equals("color")) {
					cI = i;
				} else if (parts[i].equals("normal")) {
					nI = i;
				} else if (parts[i].equals("mat")) {
					mI = i;
				}
			}
		}

		int vLength = components.length - sI;
		float[] verts = new float[vLength];
		float[] colors = new float[vLength];
		float[] normals = new float[vLength];

		int type = 0;
		int arrI = 0;
		for (int i = sI; i < components.length; i++) {
			String[] parts = components[i].split(",");
			if (type == vI) {
				verts[arrI] = Float.parseFloat(parts[0]);
				verts[arrI + 1] = Float.parseFloat(parts[1]);
				verts[arrI + 2] = Float.parseFloat(parts[2]);
				// System.out.print(String.format("v=%f,%f,%f; ", verts[arrI], verts[arrI+1],
				// verts[arrI+2]));
			} else if (type == cI) {
				colors[arrI] = Float.parseFloat(parts[0]);
				colors[arrI + 1] = Float.parseFloat(parts[1]);
				colors[arrI + 2] = Float.parseFloat(parts[2]);
				// System.out.print(String.format("c=%f,%f,%f; ", colors[arrI], colors[arrI+1],
				// colors[arrI+2]));
			} else if (type == nI) {
				normals[arrI] = Float.parseFloat(parts[0]);
				normals[arrI + 1] = Float.parseFloat(parts[1]);
				normals[arrI + 2] = Float.parseFloat(parts[2]);
				// System.out.print(String.format("n=%f,%f,%f; ", normals[arrI],
				// normals[arrI+1], normals[arrI+2]));
			} else if (type == mI) {
				colors[arrI] = Float.parseFloat(parts[0]);
			}

			type = (type + 1) % 3;
			if (type == 0) {
				arrI += 3;
				// System.out.println("");
			}
		}

		mesh.setVertices(verts);
		mesh.setColors(colors);
		mesh.setNormals(normals);
		return mesh;
	}

	/**
	 * Create a new mesh from mesh file
	 * 
	 * @param path path to plain text mesh file
	 * @return New mesh from file
	 * @throws IOException If the file could not be found or read
	 */
	public static Mesh createFromFile(String path) throws IOException {
		return createFromFile(path, new Mesh());
	}

	/**
	 * Initialize a new mesh from mesh file
	 * 
	 * @param path path to plain text mesh file
	 * @param mesh mesh to initialize
	 * @return Mesh initialized from file
	 * @throws IOException If the file could not be found or read
	 */
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
		// return createFromString(str, mesh);
		MeshData data = meshDataFromString(str);
		mesh.set(data);
		return mesh;
	}

	/**
	 * Create a new mesh from mesh resource file
	 * 
	 * @param path resource path to plain text mesh file
	 * @return New mesh from file
	 * @throws IOException If the file could not be found or read
	 */
	public static Mesh createFromResource(String resource) throws IOException {
		// BufferedReader br = new BufferedReader(new FileReader(path));
		ClassLoader classLoader = Mesh.class.getClassLoader();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resource)));
		} catch (NullPointerException e) {
			throw new IOException("Could not load mesh from resource \"" + resource + "\"");
		}
		String line = "";
		String str = "";
		while (line != null) {
			str += line;
			line = br.readLine();
		}
		br.close();
		// return createFromString(str);
		return new Mesh(meshDataFromString(str));
	}

	/*
	 * MeshData functions
	 */

	/**
	 * Create MeshData from a mesh plain text file
	 * @param str plain text mesh data
	 * @return MeshData from file
	 */
	public static MeshData meshDataFromString(String str) {
		MeshData data = new MeshData();
		String[] components = str.split(";");
		int sI = 0;
		int vI = 0;
		int cI = 1;
		int nI = 2;
		int mI = -1;

		if (components[0].contains("format=")) {
			sI++;
			String[] parts = components[0].substring(8).split(",");
			// vI = -1;
			cI = -1;
			nI = -1;
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].equals("vertex")) {
					vI = i;
				} else if (parts[i].equals("color")) {
					cI = i;
				} else if (parts[i].equals("normal")) {
					nI = i;
				} else if (parts[i].equals("mat")) {
					mI = i;
				}
			}
		}

		int vLength = components.length - sI;
		float[] verts = new float[vLength];
		float[] colors = new float[vLength];
		float[] normals = new float[vLength];

		int type = 0;
		int arrI = 0;
		for (int i = sI; i < components.length; i++) {
			String[] parts = components[i].split(",");
			if (type == vI) {
				verts[arrI] = Float.parseFloat(parts[0]);
				verts[arrI + 1] = Float.parseFloat(parts[1]);
				verts[arrI + 2] = Float.parseFloat(parts[2]);
				// System.out.print(String.format("v=%f,%f,%f; ", verts[arrI], verts[arrI+1],
				// verts[arrI+2]));
			} else if (type == cI) {
				colors[arrI] = Float.parseFloat(parts[0]);
				colors[arrI + 1] = Float.parseFloat(parts[1]);
				colors[arrI + 2] = Float.parseFloat(parts[2]);
				// System.out.print(String.format("c=%f,%f,%f; ", colors[arrI], colors[arrI+1],
				// colors[arrI+2]));
			} else if (type == nI) {
				normals[arrI] = Float.parseFloat(parts[0]);
				normals[arrI + 1] = Float.parseFloat(parts[1]);
				normals[arrI + 2] = Float.parseFloat(parts[2]);
				// System.out.print(String.format("n=%f,%f,%f; ", normals[arrI],
				// normals[arrI+1], normals[arrI+2]));
			} else if (type == mI) {
				colors[arrI] = Float.parseFloat(parts[0]);
			}

			type = (type + 1) % 3;
			if (type == 0) {
				arrI += 3;
				// System.out.println("");
			}
		}

		data.vertices = verts;
		data.colors = colors;
		data.normals = normals;
		return data;
	}

	/**
	 * Check if the provided MeshData is valid for creating a mesh
	 * @param data MeshData to check
	 * @return Valid for creating a Mesh
	 */
	public static boolean validateMeshData(MeshData data) {
		return (data.type.equals("MESH")) && (data.vertices != null) && (data.colors != null);
	}
}
