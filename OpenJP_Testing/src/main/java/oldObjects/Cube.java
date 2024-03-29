package oldObjects;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

// import vectorLibrary.Vector3D;
import org.joml.Vector3f;

public class Cube extends GL_Drawable{
	
	public Vector3f size;
	public float b;
	private float r;
	
	public Cube(Vector3f size, Vector3f pos, Vector3f rot, float b) {
		super(pos, rot);
		this.size = size;
		this.b = b;
	}
	@Override
	public void onDraw(Vector3f lRot) {
		r += 0.5;
		r %= 360d;
		rot.x = r;
		rot.y = r;
		rot.z = r;
		glBegin(GL_QUADS);
			glColor3f(0*b,1*b,1*b);
			glVertex3d(-size.x/2d, size.y/2d, size.z/2d);
			glVertex3d(-size.x/2d, size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d, size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d, size.y/2d, size.z/2d);
			glColor3f(1*b,0*b,0*b);
			glVertex3d(-size.x/2d,-size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d,-size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d, size.y/2d,-size.z/2d);
			glVertex3d(-size.x/2d, size.y/2d,-size.z/2d);
			glColor3f(0*b,1*b,0*b);
			glVertex3d(-size.x/2d,-size.y/2d,-size.z/2d);
			glVertex3d(-size.x/2d,-size.y/2d, size.z/2d);
			glVertex3d(-size.x/2d, size.y/2d, size.z/2d);
			glVertex3d(-size.x/2d, size.y/2d,-size.z/2d);
			glColor3f(0*b,0*b,1*b);
			glVertex3d( size.x/2d,-size.y/2d, size.z/2d);
			glVertex3d( size.x/2d,-size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d, size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d, size.y/2d, size.z/2d);
			glColor3f(1*b,1*b,0*b);
			glVertex3d(-size.x/2d,-size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d,-size.y/2d,-size.z/2d);
			glVertex3d( size.x/2d,-size.y/2d, size.z/2d);
			glVertex3d(-size.x/2d,-size.y/2d, size.z/2d);
			glColor3f(1*b,0*b,1*b);
			glVertex3d(-size.x/2d,-size.y/2d, size.z/2d);
			glVertex3d( size.x/2d,-size.y/2d, size.z/2d);
			glVertex3d( size.x/2d, size.y/2d, size.z/2d);
			glVertex3d(-size.x/2d, size.y/2d, size.z/2d);
		glEnd();
	}

}
