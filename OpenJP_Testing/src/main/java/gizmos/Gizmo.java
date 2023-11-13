package gizmos;

import java.awt.Color;

import objects.Transform;

/**
 * Gizmo
 */
public class Gizmo {
    
    /**
     * Renderer for the gizmo
     */
    public GizmoRenderer renderer;
    /**
     * Gizmo position, rotation, and scale
     */
    public Transform transform;

    /**
     * Create a new white gizmo
     * @param type gizmo type (Ussaly somthing from <code>GizmoType</code>)
     */
    public Gizmo(String type) {
        transform = new Transform();
        renderer = new GizmoRenderer(transform, Color.white, type);
    }
    /**
     * Create a new colored gizmo
     * @param type gizmo type (Ussaly somthing from <code>GizmoType</code>)
     * @param color gizmo color
     */
    public Gizmo(String type, Color color) {
        transform = new Transform();
        renderer = new GizmoRenderer(transform, color, type);
    }
    /**
     * Create a new colored gizmo with given transformation
     * @param type gizmo type (Ussaly somthing from <code>GizmoType</code>)
     * @param color gizmo color
     * @param transform gizmo transformation
     */
    public Gizmo(String type, Color color, Transform transform) {
        this.transform = transform;
        renderer = new GizmoRenderer(this.transform, color, type);
    }

    /**
     * Set the color of the gizmo
     * @param color gizmo color
     */
    public void setColor(Color color) {
        renderer.setColor(color);
    }
    /**
     * Get the color of the gizmo
     * @return gizmo color
     */
    public Color getColor() {
        return renderer.getColor();
    }

    /**
     * Set the size of the gizmo
     * @param size gizmo scale (applied to all axies)
     */
    public void setSize(float size) {
        transform.setScale(size, size, size);
    }

    /**
     * Set the parent of the gizmo
     * @param parent parent trnasformation
     */
    public void setParent(Transform parent) {
        transform.parent = parent;
    }
}
