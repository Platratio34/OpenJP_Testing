package gizmos;

import java.awt.Color;

public class OriginGizmo extends Gizmo {

    private GizmoRenderer originY;
    private GizmoRenderer originZ;

    public OriginGizmo() {
        super(GizmoType.ORIGIN_X, Color.red);
        originY = new GizmoRenderer(transform, Color.blue, GizmoType.ORIGIN_Y);
        originZ = new GizmoRenderer(transform, Color.green, GizmoType.ORIGIN_Z);
    }
    
    @Override
    public void setColor(Color color) { }

    public GizmoRenderer getYRenderer() {
        return originY;
    }
    public GizmoRenderer getZRenderer() {
        return originZ;
    }
}
