package gizmos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import oldObjects.Triangle;
import util.BinMesh;

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
        if(gizmo == null) {
            System.err.println("Could not load gizmo from resource \"" + resource + "\"");
            return;
        }
        // ClassLoader classLoader = Triangle.class.getClassLoader();
        // String str = "";
		// try {
		//     BufferedReader br;
		// 	br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resource)));
        //     String line = "";
        //     while (line != null) {
        //         str += line;
        //         line = br.readLine();
        //     }
        //     br.close();
		// } catch (NullPointerException e) {
		// 	System.err.println("Could not load mesh from resource \""+resource+"\"");
		// 	return;
		// } catch (IOException e) {
		// 	System.err.println("Could not load mesh from resource \""+resource+"\"");
		// 	return;
        // }
        
        // String[] components = str.split(";");
		
		// ArrayList<Float> verts = new ArrayList<Float>();
		// ArrayList<Integer> indicies = new ArrayList<Integer>();

        // boolean vertex = false;
        // boolean index = false;
        // boolean indexed = false;
		
		// for(int i = 0; i < components.length; i++) {
        //     if(components[i].contains("vertex")) {
        //         vertex = true;
        //         index = false;
        //         continue;
        //     } else if(components[i].contains("index")) {
        //         vertex = false;
        //         index = true;
        //         indexed = true;
        //         continue;
        //     }
		// 	String[] parts = components[i].split(",");
        //     if(vertex) {
        //         verts.add(Float.parseFloat(parts[0]));
        //         verts.add(Float.parseFloat(parts[1]));
        //         verts.add(Float.parseFloat(parts[2]));
        //     }
        //     if(index) {
        //         indicies.add(Integer.parseInt(parts[0]));
        //         indicies.add(Integer.parseInt(parts[1]));
        //     }
		// }

        // GizmoMesh gizmo;

        // float[] vA = new float[verts.size()];
        // for(int i = 0; i < verts.size(); i++) {
        //     vA[i] = verts.get(i);
        // }
        // if(indexed) {
        //     int[] iA = new int[indicies.size()];
        //     for(int i = 0; i < indicies.size(); i++) {
        //         iA[i] = indicies.get(i);
        //     }
        //     gizmo = new GizmoMesh(vA, iA);
        //     System.out.println("Loaded indexed gizmo "+type);
        // } else {
        //     System.out.println("Loaded non-indexed gizmo "+type);
        //     gizmo = new GizmoMesh(vA);
        // }
		
        gizmos.put(type, gizmo);
    }

    public static void preLoad(String type) {
        if(gizmos.containsKey(type)) return;
        load(type);
    }

    public static void render(String type) {
        preLoad(type);
        gizmos.get(type).render();
    }

    public static void dispose() {
        for (GizmoMesh gizmo : gizmos.values()) {
            gizmo.dispose();
        }
    }
}
