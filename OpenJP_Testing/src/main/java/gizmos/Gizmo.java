package gizmos;

import java.awt.Color;

import objects.Transform;

public class Gizmo {
    
    public GizmoRenderer renderer;
    public Transform transform;

    public Gizmo(String type) {
        transform = new Transform();
        renderer = new GizmoRenderer(transform, Color.white, type);
    }
    public Gizmo(String type, Color color) {
        transform = new Transform();
        renderer = new GizmoRenderer(transform, color, type);
    }
    public Gizmo(String type, Color color, Transform transform) {
        this.transform = transform;
        renderer = new GizmoRenderer(this.transform, color, type);
    }

    public void setColor(Color color) {
        renderer.setColor(color);
    }
    public Color getColor() {
        return renderer.getColor();
    }

    public void setSize(float size) {
        transform.setScale(size, size, size);
    }

    public void setParent(Transform parent) {
        transform.parent = parent;
    }
}
