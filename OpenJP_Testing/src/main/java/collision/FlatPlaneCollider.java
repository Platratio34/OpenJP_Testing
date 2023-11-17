package collision;

import org.joml.Vector3f;

public class FlatPlaneCollider extends Collider {

    public FlatPlaneCollider() {
        // points = new Vector3f[] {
        Vector3f p1 = new Vector3f(0.5f, 0.0f, 0.5f);
        Vector3f p2 =new Vector3f(0.5f, 0.0f, -0.5f);
        Vector3f p3 =new Vector3f(-0.5f, 0.0f, 0.5f);
        Vector3f p4 =new Vector3f(-0.5f, 0.0f, -0.5f);
        // };
        triangles = new Triangle[] {
            new Triangle(p1, p2, p3),
            new Triangle(p3, p4, p1)
        };
    }
    
}
