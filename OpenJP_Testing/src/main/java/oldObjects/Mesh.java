package oldObjects;

// import vectorLibrary.Vector3D;
import org.joml.Vector3f;

public class Mesh extends GL_Drawable {

	public Triangle[] triangles;
	
	public Mesh(Vector3f pos, Vector3f rot) {
		super(pos, rot);
		triangles = new Triangle[0];
	}
	public Mesh(Vector3f pos, Vector3f rot, Vector3f scale, Triangle[] triangles) {
		super(pos, rot);
		this.scale = scale;
		this.triangles = triangles;
	}
	
	public void setMesh(Triangle[] triangles) {
		this.triangles = triangles;
	}

	@Override
	public void onDraw(Vector3f lRot) {
//		System.out.println("Drawing mesh: "+triangles.length+" triangles");
		for(int i = 0; i < triangles.length; i++) {
			triangles[i].render(lRot);
		}
	}

}
