package objects;

import vectorLibrary.Vector3D;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Triangle {
	
	public Vector3D p0;
	public Vector3D p1;
	public Vector3D p2;
	
	public Vector3D n;
	public Vector3D c;
	
	public Triangle(Vector3D p0, Vector3D p1, Vector3D p2, Vector3D n, Vector3D c) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;

		this.n = n;
		this.c = c;
		
//		System.out.println("Created triangle: "+p0+"; "+p1+"; "+p2+"; c="+c);
	}
	
	public Triangle(String text) {
		String[] parts = text.split(";");
		if(parts.length < 5) {
			p0 = new Vector3D();
			p1 = new Vector3D();
			p2 = new Vector3D();

			n = new Vector3D();
			c = new Vector3D();
			return;
		}
		
		p0 = Vector3D.fromCoords(parts[0]);
		p1 = Vector3D.fromCoords(parts[1]);
		p2 = Vector3D.fromCoords(parts[2]);

		n = Vector3D.fromCoords(parts[3]);
		c = Vector3D.fromCoords(parts[4]);
		
//		System.out.println("Created triangle: "+p0+"; "+p1+"; "+p2+"; c="+c);
	}
	
	public void render(Vector3D lRot) {
		
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
			if(st != null && !st.equals("")) {
				mesh.add(new Triangle(st));
			}
		}
		br.close();
		System.out.println("Loaded mesh "+filename);
		return mesh.toArray(new Triangle[0]);
	}
}
