package collision;

import org.joml.Vector3f;

/**
 * Plane primitive collider
 */
public class PlaneCollider extends Collider {

    /**
     * Create a new collider with a plane from -0.5 to +0.5 in x and y.
     */
    public PlaneCollider() {
        super(new Triangle[2]);
        
        Vector3f p1 = new Vector3f(0.5f, 0.0f, 0.5f);
        Vector3f p2 =new Vector3f(0.5f, 0.0f, -0.5f);
        Vector3f p3 =new Vector3f(-0.5f, 0.0f, 0.5f);
        Vector3f p4 =new Vector3f(-0.5f, 0.0f, -0.5f);
        
        triangles[0] = new Triangle(p1, p2, p3);
        triangles[0] = new Triangle(p3, p4, p1);
    }
    
}
