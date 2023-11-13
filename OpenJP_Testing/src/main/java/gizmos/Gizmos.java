package gizmos;

import java.io.IOException;
import java.util.HashMap;

import util.BinMesh;

/**
 * Gizmo mesh cache / loader
 */
public class Gizmos {
    
    private static HashMap<String, GizmoMesh> gizmos = new HashMap<String, GizmoMesh>();

    private static void load(String type) {
        String resource = "meshes/gizmos/" + type + ".gzb";
        GizmoMesh gizmo;
        try {
            gizmo = BinMesh.gizmoFromBinResource(resource);
        } catch (IOException e) {
            System.err.println("Could not load gizmo from resource \"" + resource + "\"");
            return;
        }
        if (gizmo == null) {
            System.err.println("Could not load gizmo from resource \"" + resource + "\"");
            return;
        }

        gizmos.put(type, gizmo);
    }

    /**
     * Load a gizmo mesh if not yet loaded
     * @param type mesh type (Usually from <code>GizmoType.*</code>)
     */
    public static void preLoad(String type) {
        if (gizmos.containsKey(type))
            return;
        load(type);
    }
    
    /**
     * Render gizmo mesh
     * @param type mesh type (Usually from <code>GizmoType.*</code>)
     */
    public static void render(String type) {
        preLoad(type);
        gizmos.get(type).render();
    }

    /**
     * Dispose of all cached gizmo meshes
     */
    public static void dispose() {
        for (GizmoMesh gizmo : gizmos.values()) {
            gizmo.dispose();
        }
        gizmos.clear();
    }
}
