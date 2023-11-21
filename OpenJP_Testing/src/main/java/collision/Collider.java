package collision;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import game.Component;
import objects.Transform;
import util.MeshData;

/**
 * Triangle based collider
 */
public class Collider extends Component {

    protected Triangle[] triangles;

    protected float boundDist;

    /** The transform for this collider. Should be parented to game objects rather than replaced */
    public Transform transform;

    /**
     * Collision mask.<br>
     * <br>
     * CCollision will not occur if this mask and the other collider's mask is 0 when and-ed.
     * <br><br>
     * <h4>Examples</h4>
     * <p>
     *  If this collider's mask is <code>0b0001_0001</code> and the other's mask is <code>0b0010_0001</code> then when and-ed you get <code>0b0000_0001</code>.
     *  Because this is not zero, collision will be checked.
     * </p>
     * 
     * <p>
     *  If this collider's mask is <code>0b0001_0001</code> and the other's mask is <code>0b0010_0010</code> then when and-ed you get <code>0b0000_0000</code>.
     *  Because this <b>is</b> zero, collision will <b>NOT</b> be checked.
     * </p>
     * */
    public byte mask = 0b0000_0001;

    protected Collider() {
        transform = new Transform();
    }

    @Override
    public void onStart() {
        transform.setParent(gameObject.transform);
    }

    /**
     * Create a new collider from an array of triangles
     * 
     * @param triangles array of triangles
     */
    public Collider(Triangle[] triangles) {
        this();
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
        this();
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
        this();
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

    /**
     * Create a new collider from a mesh data object
     * @param data
     */
    public Collider(MeshData data) {
        this(data.vertices);
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
     * @param other collider to check against
     * @return If the they are colliding
     */
    public boolean checkCollision(Collider other) {
        if ((other.mask & mask) == 0x00) {
            return false;
        }
        Matrix4f thisMatrix = transform.getTransformMatrix();
        Matrix4f otherMatrix = other.transform.getTransformMatrix();
        if (!checkBound(other, thisMatrix, otherMatrix))
            return false;
        for (int i = 0; i < triangles.length; i++) {
            for (int j = 0; j < other.triangles.length; j++) {
                if (triangles[i].collides(other.triangles[j], thisMatrix, otherMatrix)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check if this collides with another collider
     * 
     * @param other collider to check against
     * @param mask collision mask to check with, and-ed with the this collider and the other collider's masks. See <code>Collider.mask</code> for details
     * @return If the they are colliding
     */
    public boolean checkCollision(Collider other, byte mask) {
        if ((other.mask & this.mask & mask) == 0x00) {
            return false;
        }
        return checkCollision(other);
    }

    /**
     * Check if the 2 colliders are within their bounding distances of each other.
     * <b>DOES NOT MEAN COLLISION</b>
     * 
     * @param other collider to check against
     * @return if distance is less than bounding distance
     */
    public boolean checkBound(Collider other) {
        Matrix4f thisMatrix = transform.getTransformMatrix();
        Matrix4f otherMatrix = other.transform.getTransformMatrix();
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
