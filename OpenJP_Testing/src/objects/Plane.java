package objects;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

import vectorLibrary.Vector2D;
import vectorLibrary.Vector3D;

public class Plane extends GL_Drawable {
	
	private Vector2D size;
	private Vector3D color;
	public Plane(Vector2D size, Vector3D pos, Vector3D rot, Vector3D color) {
		super(pos, rot);
		this.size = size;
		this.color = color;
	}

	@Override
	public void onDraw(Vector3D lRot) {
//		Vector3D tColor = color.scale(1d/(diffAngle(new Vector3D(0,1,0),lRot)/360d));
		glBegin(GL_QUADS);
			glColor3d(color.x, color.y, color.z);
			glVertex3d(-size.x/2d, 0, size.y/2d);
			glVertex3d(-size.x/2d, 0,-size.y/2d);
			glVertex3d( size.x/2d, 0,-size.y/2d);
			glVertex3d( size.x/2d, 0, size.y/2d);
		glEnd();
	}

}
