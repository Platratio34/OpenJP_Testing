package oldObjects;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

// import vectorLibrary.Vector3D;
import org.joml.Vector3f;

public abstract class GL_Drawable {
	
	public Vector3f pos;
	public Vector3f rot;
	public Vector3f scale;
	
	public GL_Drawable(Vector3f pos, Vector3f rot) {
		this.pos = pos;
		this.rot = rot;
		scale = new Vector3f();
	}
	
	public void draw(Vector3f cPos, Vector3f cRot, Vector3f lRot) {
		glLoadIdentity();
		glScaled(0.1,0.1,0.1);
		glTranslated(cPos.x, cPos.y, cPos.z);
		glRotated(cRot.x, 1, 0.0, 0.0);
		glRotated(cRot.y, 0, 1, 0.0);
		glRotated(cRot.z, 0, 0, 1);
		glTranslated(pos.x, pos.y, pos.z);
		glRotated(rot.x, 1, 0.0, 0.0);
		glRotated(rot.y, 0, 1, 0.0);
		glRotated(rot.z, 0, 0, 1);
		glScaled(scale.x,scale.y,scale.z);
		onDraw(lRot.sub(cRot));
	}
	public abstract void onDraw(Vector3f lRot);
	
	public static double diffAngle(Vector3f a0, Vector3f a1) {
		double diff = Math.abs(a0.x - a1.x);
		diff += Math.abs(a0.y - a1.y);
		diff += Math.abs(a0.z - a1.z);
		return diff;
	}
	
	public static double normalDiff(Vector3f norm, Vector3f ang) {
		double diff = 0;
		
		return diff;
	}
	
//	public static Vector3D shade(Ve)
}
