package gizmos;

import org.lwjgl.opengl.GL33;

import GLObjects.VAO;

public class GizmoMesh {
    
    private VAO vao;
    private int indexLength;
    private boolean indexMode;

    public GizmoMesh(float[] points) {
        vao = new VAO();

        vao.storeDataInAttributeList(0, points);
        indexLength = points.length;
    }
    public GizmoMesh(float[] points, int[] indecies) {
        vao = new VAO();

        vao.storeVertexIndexData(points, indecies);
        indexMode = true;
        indexLength = indecies.length;
    }

    public void render() {
        vao.bind();
		GL33.glEnableVertexAttribArray(0);
		GL33.glEnableVertexAttribArray(4);
		
		if(indexMode) {
			GL33.glDrawElements(GL33.GL_LINES, indexLength, GL33.GL_UNSIGNED_INT, 0);
		} else {
			GL33.glDrawArrays(GL33.GL_LINES, 0, indexLength);
		}
		
		GL33.glDisableVertexAttribArray(0);
		GL33.glDisableVertexAttribArray(4);
		VAO.unbind();
    }
	
	public void dispose() {
		vao.dispose();
	}
}
