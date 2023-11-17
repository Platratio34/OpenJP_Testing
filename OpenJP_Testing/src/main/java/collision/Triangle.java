package collision;

import org.joml.Vector3f;

public class Triangle {
    
    private Vector3f point1;
    private Vector3f point2;
    private Vector3f point3;

    public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
        point1 = p1;
        point2 = p2;
        point3 = p3;
    }

    public boolean collides(Triangle other) {
        return other.intersects(point1, point2) || other.intersects(point2, point3) || other.intersects(point3, point1); 
    }

    public boolean intersects(Vector3f q1, Vector3f q2) {
        if (positiveVolume(q1, point1, point1, point2) != positiveVolume(q2, point1, point2, point3)) {
            boolean s3 = positiveVolume(q1,q2,point1,point2);
            boolean s4 = positiveVolume(q1,q2,point2,point3);
            boolean s5 = positiveVolume(q1,q2,point3,point1);
            return s3 == s4 && s4 == s5;
        }
        return false;
    }
    
    private boolean positiveVolume(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        Vector3f ab = b.sub(a, new Vector3f());
        Vector3f ac = c.sub(a, new Vector3f());
        Vector3f ad = d.sub(a, new Vector3f());
        float volume = (ab.cross(ac)).dot(ad);

        return volume >= 0;
    }
}
