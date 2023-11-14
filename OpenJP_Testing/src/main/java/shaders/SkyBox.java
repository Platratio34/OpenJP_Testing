package shaders;

import objects.Mesh;
import objects.MeshRenderer;
import objects.Transform;

/**
 * Skybox renderer
 */
public class SkyBox extends MeshRenderer {

    /**
     * Create a new skybox renderer
     */
    public SkyBox() {
        super(new Transform(), null);
        mesh = new Mesh();
        float points[] = {
            -2.0f,  2.0f, -2.0f,
            -2.0f, -2.0f, -2.0f,
             2.0f, -2.0f, -2.0f,
             2.0f, -2.0f, -2.0f,
             2.0f,  2.0f, -2.0f,
            -2.0f,  2.0f, -2.0f,
            
            -2.0f, -2.0f,  2.0f,
            -2.0f, -2.0f, -2.0f,
            -2.0f,  2.0f, -2.0f,
            -2.0f,  2.0f, -2.0f,
            -2.0f,  2.0f,  2.0f,
            -2.0f, -2.0f,  2.0f,
            
             2.0f, -2.0f, -2.0f,
             2.0f, -2.0f,  2.0f,
             2.0f,  2.0f,  2.0f,
             2.0f,  2.0f,  2.0f,
             2.0f,  2.0f, -2.0f,
             2.0f, -2.0f, -2.0f,
             
            -2.0f, -2.0f,  2.0f,
            -2.0f,  2.0f,  2.0f,
             2.0f,  2.0f,  2.0f,
             2.0f,  2.0f,  2.0f,
             2.0f, -2.0f,  2.0f,
            -2.0f, -2.0f,  2.0f,
            
            -2.0f,  2.0f, -2.0f,
             2.0f,  2.0f, -2.0f,
             2.0f,  2.0f,  2.0f,
             2.0f,  2.0f,  2.0f,
            -2.0f,  2.0f,  2.0f,
            -2.0f,  2.0f, -2.0f,
            
            -2.0f, -2.0f, -2.0f,
            -2.0f, -2.0f,  2.0f,
             2.0f, -2.0f, -2.0f,
             2.0f, -2.0f, -2.0f,
            -2.0f, -2.0f,  2.0f,
             2.0f, -2.0f,  2.0f
        };
        mesh.setVertices(points);
    }
    
}
