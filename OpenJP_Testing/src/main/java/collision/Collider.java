package collision;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import game.Component;

/**
 * Triangle based collider
 */
public class Collider extends Component {

    protected Triangle[] triangles;

    protected float boundDist;

    /**
     * Create a new collider from an array of triangles
     * 
     * @param triangles array of triangles
     */
    public Collider(Triangle[] triangles) {
        this.triangles = triangles;
        computeBounds();
    }

    /**
     * Create a new collider from an array of vertices.<br>
     * <br>
     * Creates triangles out of every 3 vertices sequential.
     * 
     * @param vertices array of vertices
     */
    public Collider(Vector3f[] vertices) {
        triangles = new Triangle[vertices.length / 3];
        for (int i = 0; i < triangles.length; i++) {
            triangles[i] = new Triangle(
                    vertices[(i * 3)],
                    vertices[(i * 3) + 1],
                    vertices[(i * 3) + 2]);
        }
        computeBounds();
    }

    /**
     * Creates a new collider from an array of flatted vertices.<br>
     * <br>
     * Creates a vertex from every 3 points sequentially (ordered x,y,z).<br>
     * <br>
     * Creates triangles out of every 3 vertices sequentially.
     * 
     * @param flatVertices
     */
    public Collider(float[] flatVertices) {
        triangles = new Triangle[flatVertices.length / 9];
        for (int i = 0; i < triangles.length; i++) {
            int pI = i * 9;
            triangles[i] = new Triangle(
                    new Vector3f(flatVertices[pI + 0], flatVertices[pI + 1], flatVertices[pI + 2]),
                    new Vector3f(flatVertices[pI + 3], flatVertices[pI + 4], flatVertices[pI + 5]),
                    new Vector3f(flatVertices[pI + 6], flatVertices[pI + 7], flatVertices[pI + 8]));
        }
        computeBounds();
    }

    private void computeBounds() {
        boundDist = 0;
        for (Triangle triangle : triangles) {
            float d = triangle.getMaxDist();
            if (d > boundDist)
                boundDist = d;
        }
    }

    /**
     * Check if this collides with another collider
     * 
     * @param other
     * @return
     */
    public boolean checkCollision(Collider other) {
        Matrix4f thisMatrix = gameObject.transform.getTransformMatrix();
        Matrix4f otherMatrix = other.gameObject.transform.getTransformMatrix();
        if (!checkBound(other, thisMatrix, otherMatrix))
            return false;
        for (int i = 0; i < triangles.length; i++) {
            for (int j = 0; j < other.triangles.length; i++) {
                if (triangles[i].collides(other.triangles[j], thisMatrix, otherMatrix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the 2 colliders are within their bounding distances of each other.
     * <b>DOES NOT MEAN COLLISION</b>
     * 
     * @param other collider to check against
     * @return if distance is less than bounding distance
     */
    public boolean checkBound(Collider other) {
        Matrix4f thisMatrix = gameObject.transform.getTransformMatrix();
        Matrix4f otherMatrix = other.gameObject.transform.getTransformMatrix();
        return checkBound(other, thisMatrix, otherMatrix);
    }

    private boolean checkBound(Collider other, Matrix4f thisMatrix, Matrix4f otherMatrix) {
        float dist = thisMatrix.transformPosition(new Vector3f())
                .distance(otherMatrix.transformPosition(new Vector3f()));
        float tD = thisMatrix.getScale(new Vector3f()).length() * boundDist;
        float oD = otherMatrix.getScale(new Vector3f()).length() * other.boundDist;
        return dist <= tD + oD;
    }

}
