package game.simple;

import game.GameObject;
import objects.MeshCache;
import objects.MeshRenderer;
import shaders.Material;

public class PrimitiveCreator {
    
    public static GameObject createCube() {
        GameObject gO = new GameObject();
        MeshRenderer mR = new MeshRenderer(MeshCache.getMesh("meshes/matCube.bin"));
        mR.materials.setMaterial(0, new Material());
        gO.addComponent(mR);
        return gO;
    }

    public static GameObject createPlane() {
        GameObject gO = new GameObject();
        MeshRenderer mR = new MeshRenderer(MeshCache.getMesh("meshes/plane.bin"));
        mR.materials.setMaterial(0, new Material());
        gO.addComponent(mR);
        return gO;
    }
}
