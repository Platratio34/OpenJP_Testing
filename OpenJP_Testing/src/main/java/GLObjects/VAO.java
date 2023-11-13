package GLObjects;

import java.util.ArrayList;

import org.lwjgl.opengl.GL44;

/**
 * Vertex Array Object<br><br>
 * Stores vertices, color, normal, uvs, and indices for a mesh.
 */
public class VAO {
	
	private int id;
	
	private ArrayList<ABO> abos;

	/** Attribute for vertex data */
	public static final int ATTRIB_VERTEX = 0;
	/** Attribute for color data */
	public static final int ATTRIB_COLOR = 1;
	/** Attribute for normal data */
	public static final int ATTRIB_NORMAL = 2;
	/** Attribute for uv data */
	public static final int ATTRIB_UV = 3;
	/** Attribute for index data */
	public static final int ATTRIB_INDEX = 4;

	/** Currently bound VAO */
	public static int currentBoundVAO = 0;
	
	/**
	 * Create and allocate a new VAO
	 */
	public VAO() {
		id = GL44.glGenVertexArrays();
		abos = new ArrayList<ABO>();
	}
	
	/**
	 * Store data into an attribute list. Per-vertex size is 3.
	 * @param attrib attribute index (see <code>VAO.ATTRIB_*</code>)
	 * @param data data to store
	 */
	public void storeDataInAttributeList(int attrib, float[] data) {
		storeDataInAttributeList(attrib, data, 3);
	}
	/**
	 * Store data into an attribute list. Per-vertex size is 3.
	 * @param attrib attribute index (see <code>VAO.ATTRIB_*</code>)
	 * @param data data to store
	 * @param size per-vertex size (number of values per vertex, ie 3 for vec3 or 2 for vec2)
	 */
	public void storeDataInAttributeList(int attrib, float[] data, int size) {
		bind();
		ABO abo = new ABO(GL44.GL_ARRAY_BUFFER);
		abo.fill(data);
		abos.add(abo);
		GL44.glEnableVertexAttribArray(attrib);
		GL44.glVertexAttribPointer(attrib, size, GL44.GL_FLOAT, false, 0, 0);
		abo.unbind();
	}
	
	/**
	 * Store vertex and index data
	 * @param vertices flattened array of vector 3 vertex data (ordered x, y, z)
	 * @param indices flattened array of vector 2 index data
	 */
	public void storeVertexIndexData(float[] vertices, int[] indices) {
		bind();
		ABO vbo = new ABO(GL44.GL_ARRAY_BUFFER);
		vbo.fill(vertices);
		abos.add(vbo);
		GL44.glEnableVertexAttribArray(ATTRIB_VERTEX);
		GL44.glVertexAttribPointer(ATTRIB_VERTEX, 3, GL44.GL_FLOAT, false, 0, 0);
		ABO ebo = new ABO(GL44.GL_ELEMENT_ARRAY_BUFFER);
		ebo.fill(indices);
		abos.add(ebo);
		GL44.glEnableVertexAttribArray(ATTRIB_INDEX);
		GL44.glVertexAttribPointer(ATTRIB_INDEX, 2, GL44.GL_UNSIGNED_INT, false, 0, 0);
		vbo.unbind();
	}
	
	/**
	 * Store color data
	 * @param colors flattened list of color data (ordered r, g, b)
	 */
	public void storeColorData(float[] colors) {
		storeDataInAttributeList(ATTRIB_COLOR, colors, 3);
	}
	/**
	 * Store normal data
	 * @param colors flattened list of normal data (ordered x, y, z)
	 */
	public void storeNormalData(float[] normals) {
		storeDataInAttributeList(ATTRIB_NORMAL, normals, 3);
	}
	/**
	 * Store UV data
	 * @param colors flattened list of UV data (ordered x, y)
	 */
	public void storeUVData(float[] uvs) {
		storeDataInAttributeList(ATTRIB_UV, uvs, 2);
	}
	
	/**
	 * Bind the VAO
	 */
	public void bind() {
		if(currentBoundVAO == id) return;
		GL44.glBindVertexArray(id);
		currentBoundVAO = id;
	}
	/**
	 * Unbind all VAOs
	 */
	public static void unbind() {
		GL44.glBindVertexArray(0);
		currentBoundVAO = 0;
	}
	
	/**
	 * Dispose of the VAO and all associated ABOs
	 */
	public void dispose() {
		GL44.glDeleteVertexArrays(id);
		for (ABO abo : abos) {
			abo.dispose();
		}
	}
	
	/**
	 * Get the OpenGL id of the VAO
	 */
	public int getId() {
		return id;
	}
}
