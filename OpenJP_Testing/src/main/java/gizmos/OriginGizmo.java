package gizmos;

import java.awt.Color;

/**
 * Colored origin gizmo
 */
public class OriginGizmo extends Gizmo {

    private GizmoRenderer originY;
    private GizmoRenderer originZ;

    /**
     * Create a colored origin gizmo
     */
    public OriginGizmo() {
        super(GizmoType.ORIGIN_X, Color.red);
        originY = new GizmoRenderer(transform, Color.blue, GizmoType.ORIGIN_Y);
        originZ = new GizmoRenderer(transform, Color.green, GizmoType.ORIGIN_Z);
    }
    
    /**
     * DISABLED.<br>
     * <br>
     * <code>setColor()</code> has not effect on origin gizmos
     */
    @Override
    public void setColor(Color color) { }
    
    public GizmoRenderer getYRenderer() {
        return originY;
    }
    public GizmoRenderer getZRenderer() {
        return originZ;
    }
}
