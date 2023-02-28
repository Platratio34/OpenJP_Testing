package objects;

import vectorLibrary.Vector3D;

public class Mesh extends GL_Drawable {

	public Triangle[] triangles;
	
	public Mesh(Vector3D pos, Vector3D rot) {
		super(pos, rot);
		triangles = new Triangle[0];
	}
	public Mesh(Vector3D pos, Vector3D rot, Vector3D scale, Triangle[] triangles) {
		super(pos, rot);
		this.scale = scale;
		this.triangles = triangles;
	}
	
	public void setMesh(Triangle[] triangles) {
		this.triangles = triangles;
	}

	@Override
	public void onDraw(Vector3D lRot) {
//		System.out.println("Drawing mesh: "+triangles.length+" triangles");
		for(int i = 0; i < triangles.length; i++) {
			triangles[i].render(lRot);
		}
	}

}
