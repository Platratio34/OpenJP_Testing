package collision;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import game.Component;

/**
 * Triangle bassed collider
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
     * Create a new collider from an array of verticies.<br>
     * <br>
     * Creates triangles out of every 3 verticies sequentialy.
     * 
     * @param points array of verticies
     */
    public Collider(Vector3f[] points) {
        triangles = new Triangle[points.length / 3];
        for (int i = 0; i < triangles.length; i++) {
            triangles[i] = new Triangle(
                    points[(i * 3)],
                    points[(i * 3) + 1],
                    points[(i * 3) + 2]);
        }
        computeBounds();
    }

    /**
     * Creates a new collider from an array of flattend verticies.<br>
     * <br>
     * Creates a vertex from every 3 points sequentialy (orderd x,y,z).<br>
     * <br>
     * Creates triangles out of every 3 verticies sequentialy.
     * 
     * @param flatPoints
     */
    public Collider(float[] flatPoints) {
        triangles = new Triangle[flatPoints.length / 9];
        for (int i = 0; i < triangles.length; i++) {
            int pI = i * 9;
            triangles[i] = new Triangle(
                    new Vector3f(flatPoints[pI + 0], flatPoints[pI + 1], flatPoints[pI + 2]),
                    new Vector3f(flatPoints[pI + 3], flatPoints[pI + 4], flatPoints[pI + 5]),
                    new Vector3f(flatPoints[pI + 6], flatPoints[pI + 7], flatPoints[pI + 8]));
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
     * Check if the 2 colliders are within their bounding distances of
     * eachother.<b>DOES NOT MEAN COLLISION</b>
     * 
     * @param other collider to check agains
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
