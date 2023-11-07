package test;

import java.io.IOException;

import oldObjects.GL_Window;
import oldObjects.Mesh;
import oldObjects.Triangle;
// import vectorLibrary.Vector3D;
import org.joml.Vector3f;

public class TestWindow extends GL_Window {
	
	private Triangle[] cubeMesh;
	private Triangle[] pyramidMesh;
	private Triangle[] planeMesh;
	private Triangle[] plane2Mesh;
	
	private Mesh[] pyramids;

	@Override
	protected void onInit() {
		try {
        	cubeMesh = Triangle.loadMeshResource("meshes/cube.mesh");
        	pyramidMesh = Triangle.loadMeshResource("meshes/pyramid.mesh");
        	planeMesh = Triangle.loadMeshResource("meshes/plane.mesh");
        	plane2Mesh = Triangle.loadMeshResource("meshes/plane2.mesh");
        	System.out.println("Meshes loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		drawables.add(new Plane(new Vector2D(30,30), new Vector3f(0,-5.1,0), new Vector3f(0,0,0), new Vector3f(0.1,0.5,0.05)));
//        drawables.add(new Plane(new Vector2D(10,10), new Vector3f(0,-5,0), new Vector3f(0,0,0), new Vector3f(0.2,0.2,0.2)));
//        
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,-3,-3), new Vector3f(0,0,0), 1f));
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,3,-3), new Vector3f(0,0,0), 1f));
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,-3,-3), new Vector3f(0,0,0), 1f));
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,3,-3), new Vector3f(0,0,0), 1f));
//
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,-3,3), new Vector3f(0,0,0), 1f));
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,3,3), new Vector3f(0,0,0), 1f));
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,-3,3), new Vector3f(0,0,0), 1f));
//        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,3,3), new Vector3f(0,0,0), 1f));

        drawables.add(new Mesh(new Vector3f(0,-5.1f,0), new Vector3f(0,0,0), new Vector3f(30,30,30), plane2Mesh));
        drawables.add(new Mesh(new Vector3f(0,-5,0), new Vector3f(0,0,0), new Vector3f(10,10,10), planeMesh));
        
        drawables.add(new Mesh(new Vector3f(-3,-3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
        drawables.add(new Mesh(new Vector3f(-3,-3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
        drawables.add(new Mesh(new Vector3f(3,-3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
        drawables.add(new Mesh(new Vector3f(3,-3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
        
        pyramids = new Mesh[] {
        		new Mesh(new Vector3f(-3,3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh),
        		new Mesh(new Vector3f(-3,3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh),
        		new Mesh(new Vector3f(3,3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh),
        		new Mesh(new Vector3f(3,3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh)
        };
        drawables.add(pyramids[0]);
        drawables.add(pyramids[1]);
        drawables.add(pyramids[2]);
        drawables.add(pyramids[3]);
        
        
        cRot.x = -45;
        
        cPos.x = 0;
        cPos.y = 0;
        cPos.z = 0;
        
        System.out.println("Window loaded");
	}
	
	private float rot = 0f;
	
	@Override
	protected void onLoop() {
        
//        cRot.y += 0.3f;
//        cRot.y %= 360f;
        
        rot = (rot+0.3f)%360;
        for(int i = 0; i < pyramids.length; i++) {
        	pyramids[i].rot.y = rot;
        }
	}

}
