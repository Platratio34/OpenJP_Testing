package oldObjects;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

// import vectorLibrary.Vector2D;
import org.joml.Vector2f;
// import vectorLibrary.Vector3D;
import org.joml.Vector3f;

public class Plane extends GL_Drawable {
	
	private Vector2f size;
	private Vector3f color;
	public Plane(Vector2f size, Vector3f pos, Vector3f rot, Vector3f color) {
		super(pos, rot);
		this.size = size;
		this.color = color;
	}

	@Override
	public void onDraw(Vector3f lRot) {
//		Vector3f tColor = color.scale(1d/(diffAngle(new Vector3f(0,1,0),lRot)/360d));
		glBegin(GL_QUADS);
			glColor3d(color.x, color.y, color.z);
			glVertex3d(-size.x/2d, 0, size.y/2d);
			glVertex3d(-size.x/2d, 0,-size.y/2d);
			glVertex3d( size.x/2d, 0,-size.y/2d);
			glVertex3d( size.x/2d, 0, size.y/2d);
		glEnd();
	}

}
