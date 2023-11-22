package util;

import org.joml.Vector2f;

public class CubicBezier implements Curve, Curve2D {
    
    public Vector2f p0 = new Vector2f(0f, 0f);
    public Vector2f p1 = new Vector2f(0.5f, 0f);
    public Vector2f p2 = new Vector2f(0.5f, 1f);
    public Vector2f p3 = new Vector2f(1f, 1f);

    private Vector2f[] points;

    public CubicBezier() {

    }

    public void recalculatePoints() {
        points = new Vector2f[101];
        for (int i = 0; i <= 100; i++) {
            points[i] = evaluate2D(i / 100f);
        }
    }

    @Override
    public Vector2f evaluate2D(float time) {
        float it = 1-time;
        // float o = p0.mul((float) Math.pow(it, 3), new Vector2f());
        Vector2f a1 = p0.mul(it, new Vector2f()).add(p1.mul(time, new Vector2f()));
        Vector2f a2 = p1.mul(it, new Vector2f()).add(p2.mul(time, new Vector2f()));
        Vector2f a3 = p2.mul(it, new Vector2f()).add(p3.mul(time, new Vector2f()));

        Vector2f b1 = a1.mul(it, new Vector2f()).add(a2.mul(time, new Vector2f()));
        Vector2f b2 = a2.mul(it, new Vector2f()).add(a3.mul(time, new Vector2f()));
        
        Vector2f c = b1.mul(it, new Vector2f()).add(b2.mul(time, new Vector2f()));

        return c;
    }

    @Override
    public float evaluate(float time) {
        if (points == null)
            recalculatePoints();
        if (time <= 0)
            return points[0].y;
        if (time >= 1)
            return points[100].y;
        int i = 0;
        int j = 0;
        
        if (time <= 0.5) {
            for (j = 0; j <= 100; j++) {
                if (points[j].x == time)
                    return points[j].y;
                if (points[j].x > time) {
                    i = j - 1;
                    break;
                }
            }
        } else {
            for (i = 100; i >= 0; i--) {
                if (points[i].x == time)
                    return points[i].y;
                if (points[i].x < time) {
                    j = i + 1;
                    break;
                }
            }
        }
        float dx = points[j].x - points[i].x;
        float t = (time - points[i].x) / dx;
        float dy = points[j].y - points[i].y;
        return points[i].y + (dy * t);
    }
    
}
