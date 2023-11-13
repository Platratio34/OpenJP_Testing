package gizmos;

import org.lwjgl.opengl.GL44;

import GLObjects.VAO;

/**
 * Mesh for gizmo.<br><br>
 * <b>Shader independent</b>
 */
public class GizmoMesh {
    
    /**
     * Vertex Array Object for mesh
     */
    private VAO vao;
    /**
     * Number of verticies or indiceis depending on mode
     */
    private int indexLength;
    /**
     * If the mesh should be renderd using indicies
     */
    private boolean indexMode;

    /**
     * Create a new gizmo mesh in vertex mode
     * @param points flatend list of verticies, (orderd x, y, z)
     */
    public GizmoMesh(float[] points) {
        vao = new VAO();

        vao.storeDataInAttributeList(0, points);
        indexLength = points.length;
    }
    /**
     * Create a new gizmo mesh in index mode
     * @param points flatend list of verticies, (orderd x, y, z)
     * @param indecies flatend list of indicies, (orderd v1, v2)
     */
    public GizmoMesh(float[] points, int[] indecies) {
        vao = new VAO();

        vao.storeVertexIndexData(points, indecies);
        indexMode = true;
        indexLength = indecies.length;
    }

    /**
     * Render the gizmo mesh
     */
    public void render() {
        vao.bind();
		
		if(indexMode) {
			GL44.glDrawElements(GL44.GL_LINES, indexLength, GL44.GL_UNSIGNED_INT, 0);
		} else {
			GL44.glDrawArrays(GL44.GL_LINES, 0, indexLength);
		}

		VAO.unbind();
    }
	
    /**
     * Dispose of the gizmo mesh
     */
	public void dispose() {
		vao.dispose();
	}
}
