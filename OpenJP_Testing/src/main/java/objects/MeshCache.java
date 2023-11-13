package objects;

import java.util.HashMap;

import util.BinMesh;

import java.io.IOException;

/**
 * Mesch cahce to reduce constant reloading meshes from file
 */
public class MeshCache {
    
    private static HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();

    /**
     * Get mesh by resouce path
     * @param path resouce path
     * @return Mesh OR <code>null</code> if the mesh could not be loaded
     */
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

    /**
     * Load a mesh by resouce path
     * @param path resouce path
     * @throws IOException if the file could not be found or loaded OR if the file type is unknown
     */
    public static void load(String path) throws IOException{
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
        throw new IOException("Unknown file type");
    }

    /**
     * Check if the given path has been loaded yet
     */
    public boolean hasMesh(String path) {
        return meshes.containsKey(path);
    }
}
