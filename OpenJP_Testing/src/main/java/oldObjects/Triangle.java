package oldObjects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;

// import vectorLibrary.Vector3D;
import org.joml.Vector3f;

public class Triangle {
	
	public Vector3f p0;
	public Vector3f p1;
	public Vector3f p2;
	
	public Vector3f n;
	public Vector3f c;
	
	public Triangle(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f n, Vector3f c) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;

		this.n = n;
		this.c = c;
		
//		System.out.println("Created triangle: "+p0+"; "+p1+"; "+p2+"; c="+c);
	}

	private Vector3f fromCoords(String str) {
		Vector3f v = new Vector3f();
		return v;
	}
	
	public Triangle(String text) {
		String[] parts = text.split(";");
		if(parts.length < 5) {
			p0 = new Vector3f();
			p1 = new Vector3f();
			p2 = new Vector3f();

			n = new Vector3f();
			c = new Vector3f();
			return;
		}
		
		p0 = fromCoords(parts[0]);
		p1 = fromCoords(parts[1]);
		p2 = fromCoords(parts[2]);

		n = fromCoords(parts[3]);
		c = fromCoords(parts[4]);
		
//		System.out.println("Created triangle: "+p0+"; "+p1+"; "+p2+"; c="+c);
	}
	
	public void render(Vector3f lRot) {
		
		double r = c.x;
		double g = c.y;
		double b = c.z;
		
//		double br = n.dot(lRot);
//		r /= br;
//		g /= br;
//		b /= br;
		
		glBegin(GL_TRIANGLES);
			glColor3d(r,g,b);
			glVertex3d(p0.x,p0.y,p0.z);
			glVertex3d(p1.x,p1.y,p1.z);
			glVertex3d(p2.x,p2.y,p2.z);
		glEnd();
	}
	
	public static Triangle[] loadMesh(String filename) throws IOException {
		ArrayList<Triangle> mesh = new ArrayList<Triangle>();

		BufferedReader br = new BufferedReader(new FileReader(filename));

		String st = "";
		while (st != null) {
			st = br.readLine();
			if (st != null && !st.equals("")) {
				mesh.add(new Triangle(st));
			}
		}
		br.close();
		System.out.println("Loaded mesh " + filename);
		return mesh.toArray(new Triangle[0]);
	}
	public static Triangle[] loadMeshResource(String resource) throws IOException {
		ArrayList<Triangle> mesh = new ArrayList<Triangle>();

		// BufferedReader br = new BufferedReader(new FileReader(filename));
		ClassLoader classLoader = Triangle.class.getClassLoader();
		BufferedReader br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resource)));

		String st = "";
		while (st != null) {
			st = br.readLine();
			if (st != null && !st.equals("")) {
				mesh.add(new Triangle(st));
			}
		}
		br.close();
		System.out.println("Loaded mesh " + resource);
		return mesh.toArray(new Triangle[0]);
	}
	
}
