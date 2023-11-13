package gizmos;

import java.awt.Color;

import objects.Renderer;
import objects.Transform;
import shaders.Material;

/**
 * Renderer for gizmos (line only meshes)<br><br>
 * <b>Shader specific</b>
 */
public class GizmoRenderer extends Renderer {

    /**
     * Gizmo type
     */
    protected String type;

    /**
     * Create a gizmo render with given transform and color
     * @param transform gizmo transformation
     * @param color gizmo color
     * @param type gizmo type (Usually something from <code>GizmoType</code>)
     */
    public GizmoRenderer(Transform transform, Color color, String type) {
        super(transform);
        materials.setMaterial(0, new Material(color));
        setType(type);
    }

    /**
     * Set the color of the gizmo
     * @param color gizmo color
     */
    public void setColor(Color color) {
        materials.getMaterial(0).setColor(color);
    }
    /**
     * Get the color of gizmo
     * @return
     */
    public Color getColor() {
        return materials.getMaterial(0).getColor();
    }
    
    /**
     * Set the gizmo type. May cause file read.
     * @param type gizmo type (Usually something from <code>GizmoType</code>)
     */
    public void setType(String type) {
        this.type = type;
        Gizmos.preLoad(type);
    }
    /**
     * Get the type of the gizmo
     * @return gizmo type
     */
    public String getType() {
        return type;
    }

    @Override
    protected void onRender() {
        Gizmos.render(type);
    }
}
