package game.simple;

import collision.Collider;
import collision.PlaneCollider;
import game.GameObject;
import objects.MeshCache;
import objects.MeshRenderer;
import shaders.Material;

/**
 * Collection of static methods for creating various primitive shapes as game objects
 */
public class PrimitiveCreator {
    
    /**
     * Create a basic cube game object
     * @return A new game object with a cube mesh renderer
     */
    public static GameObject createCube() {
        GameObject gO = new GameObject();
        MeshRenderer mR = new MeshRenderer(MeshCache.getMesh("meshes/matCube.bin"));
        mR.materials.setMaterial(0, new Material());
        gO.addComponent(mR);
        return gO;
    }

    /**
     * Create a basic plane game object
     * @return A new game object with a plane mesh renderer
     */
    public static GameObject createPlane() {
        GameObject gO = new GameObject();
        MeshRenderer mR = new MeshRenderer(MeshCache.getMesh("meshes/plane.bin"));
        mR.materials.setMaterial(0, new Material());
        gO.addComponent(mR);
        return gO;
    }

    /**
     * Create a basic plane game object with collision
     * @return A new game object with a plane mesh renderer and collider
     */
    public static GameObject createPlaneCollision() {
        GameObject gO = new GameObject();
        MeshRenderer mR = new MeshRenderer(MeshCache.getMesh("meshes/plane.bin"));
        mR.materials.setMaterial(0, new Material());
        Collider collider = new PlaneCollider();
        gO.addComponent(collider);
        gO.addComponent(mR);
        return gO;
    }
}
