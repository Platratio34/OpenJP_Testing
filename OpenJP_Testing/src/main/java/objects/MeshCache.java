package objects;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import meshLoaders.ObjLoader;
import util.BinMesh;
import util.MeshData;

import java.io.IOException;

/**
 * Mesh cache to reduce constant reloading meshes from file
 */
public class MeshCache {
    
    private static HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();
    private static HashMap<String, MeshData> meshData = new HashMap<String, MeshData>();

    /**
     * Get mesh by resource path.<br>
     * <br>
     * Tries to load the mesh if not yet loaded
     * @param path resource path
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
     * Get mesh by resource path
     * @param path resource path
     * @return Mesh data OR <code>null</code> if the data could not be loaded
     */
    public static MeshData getMeshData(String path) {
        if (!meshData.containsKey(path)) {
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
        return meshData.get(path);
    }

    /**
     * Load a mesh by resource path
     * @param path resource path
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
        } else if (path.contains(".bin")) {
            MeshData data = BinMesh.meshDataFromBinResource(path);
            if (data == null) {
                throw new IOException("Unable to load mesh; Unknown error");
            }
            Mesh mesh = new Mesh(data);
            meshes.put(path, mesh);
            meshData.put(path, data);
            return;
        } else if (path.contains(".obj")) {
            ClassLoader classLoader = MeshCache.class.getClassLoader();
            BufferedReader doorReader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(path)));
            MeshData data = ObjLoader.parseFile(doorReader);
            // MeshData data = BinMesh.meshDataFromBinResource(path);
            if (data == null) {
                throw new IOException("Unable to load mesh; Unknown error");
            }
            Mesh mesh = new Mesh(data);
            meshes.put(path, mesh);
            meshData.put(path, data);
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

    /**
     * Dispose of all cached meshes and clears map
     */
    public static void dispose() {
        for (Mesh mesh : meshes.values()) {
            mesh.dispose();
        }
        meshes.clear();
    }
}
