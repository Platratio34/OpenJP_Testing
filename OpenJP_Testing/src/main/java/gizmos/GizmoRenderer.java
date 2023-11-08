package gizmos;

import java.awt.Color;

import objects.Renderer;
import objects.Transform;
import shaders.Material;

public class GizmoRenderer extends Renderer {

    private String type;

    public GizmoRenderer(Transform transform, Color color, String type) {
        super(transform);
        materials.setMaterial(0, new Material(color));
        setType(type);
    }

    public void setColor(Color color) {
        materials.getMaterial(0).setColor(color);
    }

    public Color getColor() {
        return materials.getMaterial(0).getColor();
    }
    
    public void setType(String type) {
        this.type = type;
        Gizmos.preLoad(type);
    }

    public String getType() {
        return type;
    }

    public void render() {
        if (!visible)
            return;
        super.render();
        Gizmos.render(type);
    }
}
