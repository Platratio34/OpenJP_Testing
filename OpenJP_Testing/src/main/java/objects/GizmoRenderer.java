package objects;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL33;

import shaders.Material;

public class GizmoRenderer extends Renderer {

    private String type;

    private static HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();

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
        if (!meshes.containsKey(type)) {
            try {
                meshes.put(type, Mesh.createFromResource("meshes/gizmos/" + type + ".mesh"));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public String getType() {
        return type;
    }

    public void render() {
        if (!visible)
            return;
        super.render();
        if (meshes.containsKey(type)) {
            meshes.get(type).render();
        }
    }
    
    public static void dispose() {
        for (Mesh mesh : meshes.values()) {
            mesh.dispose();
        }
    }
}
