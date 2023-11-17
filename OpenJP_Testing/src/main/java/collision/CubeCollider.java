package collision;

import objects.MeshCache;

public class CubeCollider extends Collider {

    public CubeCollider() {
        super(MeshCache.getMeshData("meshes/matCube.bin"));
    }
    
}
