package collision;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Triangle used for collision detection
 */
public class Triangle {
    
    private Vector3f point1;
    private Vector3f point2;
    private Vector3f point3;

    /**
     * Create a new triangle for collision detection
     * @param p1 point 1
     * @param p2 point 2
     * @param p3 point 3
     */
    public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
        point1 = p1;
        point2 = p2;
        point3 = p3;
    }

    /**
     * Check if another triangle intersects this one
     * @param other triangle to check for collision with
     * @return If the triangles collide
     */
    public boolean collides(Triangle other) {
        return other.intersects(point1, point2) || other.intersects(point2, point3) || other.intersects(point3, point1); 
    }
    /**
     * Check if another triangle intersects this one
     * @param other triangle to check for collision with
     * @param thisMatrix transformation matrix for this triangle
     * @param otherMatrix transformation for the other triangle
     * @return If the triangles collide
     */
    public boolean collides(Triangle other, Matrix4f thisMatrix, Matrix4f otherMatrix) {
        Vector3f p1 = thisMatrix.transformPosition(new Vector3f(point1));
        Vector3f p2 = thisMatrix.transformPosition(new Vector3f(point2));
        Vector3f p3 = thisMatrix.transformPosition(new Vector3f(point3));
        return other.intersects(p1, p2, otherMatrix) || other.intersects(p2, p3, otherMatrix) || other.intersects(p3, p1, otherMatrix); 
    }

    /**
     * Check if a given line segment (q1 -> q2) intersects with this triangle
     * @param q1 line segment point 1
     * @param q2 line segment point  2
     * @return If the line segment intersects
     */
    public boolean intersects(Vector3f q1, Vector3f q2) {
        if (positiveVolume(q1, point1, point2, point3) != positiveVolume(q2, point1, point2, point3)) {
            boolean s3 = positiveVolume(q1,q2,point1,point2);
            boolean s4 = positiveVolume(q1,q2,point2,point3);
            boolean s5 = positiveVolume(q1,q2,point3,point1);
            return s3 == s4 && s4 == s5;
        }
        return false;
    }
    /**
     * Check if a given line segment (q1 -> q2) intersects with this triangle
     * @param q1 line segment point 1
     * @param q2 line segment point  2
     * @param thisMatrix transformation matrix for this triangle
     * @return If the line segment intersects
     */
    public boolean intersects(Vector3f q1, Vector3f q2, Matrix4f thisMatrix) {
        Vector3f p1 = thisMatrix.transformPosition(new Vector3f(point1));
        Vector3f p2 = thisMatrix.transformPosition(new Vector3f(point2));
        Vector3f p3 = thisMatrix.transformPosition(new Vector3f(point3));
        if (positiveVolume(q1, p1, p2, p3) != positiveVolume(q2, p1, p2, p3)) {
            boolean s3 = positiveVolume(q1, q2, p1, p2);
            boolean s4 = positiveVolume(q1, q2, p2, p3);
            boolean s5 = positiveVolume(q1, q2, p3, p1);
            return s3 == s4 && s4 == s5;
        }
        return false;
    }
    
    /**
     * Check if the volume of the tetrahedron abcd is positive or negative
     * @param a
     * @param b
     * @param c
     * @param d
     * @return Sign of the volume ( V >= 0 )
     */
    private boolean positiveVolume(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        Vector3f ab = b.sub(a, new Vector3f());
        Vector3f ac = c.sub(a, new Vector3f());
        Vector3f ad = d.sub(a, new Vector3f());
        float volume = (ab.cross(ac)).dot(ad);

        return volume >= 0;
    }

    /**
     * Get the maximum distance from the origin for all points in this triangle
     * @return Maximum distance from the origin
     */
    public float getMaxDist() {
        float d1 = point1.length();
        float d2 = point1.length();
        float d3 = point1.length();
        if(d1 > d2 && d1 > d3) return d1;
        if(d2 > d1 && d2 > d3) return d3;
        return d3;
    }
}
