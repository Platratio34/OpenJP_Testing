package objects;

import java.util.HashMap;

import util.BinMesh;

import java.io.IOException;

public class MeshCache {
    
    private static HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();

    public static Mesh getMesh(String path) {
        if (!meshes.containsKey(path)) {
            try {
                load(path);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return meshes.get(path);
    }

    public static void load(String path) throws IOException, Exception {
        if (meshes.containsKey(path))
            return;
        if (path.contains(".mesh")) {
            Mesh mesh = Mesh.createFromResource(path);
            if (mesh == null) {
                throw new IOException("Unable to load mesh; Unknown error");
            }
            meshes.put(path, mesh);
            return;
        }
        if (path.contains(".bin")) {
            Mesh mesh = BinMesh.meshFromBinResource(path);
            if (mesh == null) {
                throw new IOException("Unable to load mesh; Unknown error");
            }
            meshes.put(path, mesh);
            return;
        }
        throw new Exception("Unknown file type");
    }
}
