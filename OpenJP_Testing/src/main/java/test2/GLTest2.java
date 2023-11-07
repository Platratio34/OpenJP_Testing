package test2;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector3f;

import lighting.Light;
import lighting.LightingSettings;
import objects.Camera;
import objects.Mesh;
import objects.Renderer;
import objects.Texture2D;
import objects.Transform;
import shaders.Material;
import windows.Window;
import windows.WindowLoopRunnable;
import windows.KeyboardEvent;
import windows.MouseEvent;

public class GLTest2 implements WindowLoopRunnable, KeyboardEvent, MouseEvent {

//	static ShaderProgram shader;
//	static long window;
//	
//	static int width = 800;
//	static int height = 600;
	
	Mesh testMesh;
	Mesh matCubeMesh;
	
	LightingSettings lighting;
	
	float t = 0;
	
	Camera camera;
	
//	static HashMap<Integer, Renderer> renderers;
//	protected static int nextId = 0;
	
	Transform p1;
	Transform p2;
	
	Material mat1 = new Material(new Color(0.5f, 0.5f, 0.4f), 0.0f);
	Material mat2 = new Material(new Color(0.0f, 0.75f, 0.4f), 0.0f);
	Material mat3 = new Material(new Color(0.0f, 0.0f, 0.9f), 0.0f);
	
	Window window;
	
	List<Renderer> boxes = new ArrayList<Renderer>();
	Renderer testR;
	Light light;

	Texture2D texture;

	private boolean mouseB3Down = false;
	private boolean moveMod = false;
	private Vector2d mouseLPos = new Vector2d();

	Material material;
    
	public static void main(String[] args) {
		GLTest2 glTest = new GLTest2();
		glTest.run();
	}
	public GLTest2() {
		window = new Window("Open GL Test");
		window.addLoopRunnable(this);
		window.addKeyboardListener(this);
		window.addMouseListener(this);
		
		camera = window.camera;
		lighting = window.lightingSettings;
		
        camera.transform.setPosition(3,5,3);
        camera.transform.setRotation(45, -45, 0);
        
        try {
			testMesh = Mesh.createFromResource("meshes/newCube.mesh");
			matCubeMesh = Mesh.createFromResource("meshes/matCube.mesh");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        
        p1 = new Transform();
        p2 = new Transform();
        
        Transform t1 = new Transform();
        t1.setPosition(0.0f, 0.0f, 0.0f);
        t1.setScale(10.0f, 0.1f, 0.1f);
        t1.parent = p1;
        Renderer r1 = new Renderer(matCubeMesh, t1);
        // r1.setColors(colors2);
		r1.materials.setMaterial(0, mat2);
        window.addRenderer(r1);
        
        Transform t2 = new Transform();
        t2.setPosition(0.0f, 0.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        Renderer r2 = new Renderer(matCubeMesh, t2);
        // r2.setColors(colors2);
		r2.materials.setMaterial(0, mat2);
        window.addRenderer(r2);
        
        Transform t3 = new Transform();
        t3.setPosition(0.0f, 1.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        Renderer r3 = new Renderer(matCubeMesh, t3);
        // r3.setColors(colors3);
		r3.materials.setMaterial(0, mat3);
        window.addRenderer(r3);
        
        Transform t4 = new Transform();
        t4.setPosition(0.0f, 1.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        Renderer r4 = new Renderer(matCubeMesh, t4);
        // r4.setColors(colors3);
		r4.materials.setMaterial(0, mat3);
        window.addRenderer(r4);
        
        t1 = new Transform();
        t1.setPosition(0.0f, 2.0f, 0.0f);
        t1.setScale(10.0f, 0.1f, 0.1f);
        t1.parent = p1;
        r1 = new Renderer(matCubeMesh, t1);
        // r1.setColors(colors2);
		r1.materials.setMaterial(0, mat2);
        window.addRenderer(r1);
        
        t2 = new Transform();
        t2.setPosition(0.0f, 2.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        r2 = new Renderer(matCubeMesh, t2);
        // r2.setColors(colors2);
		r2.materials.setMaterial(0, mat2);
        window.addRenderer(r2);
        
        t3 = new Transform();
        t3.setPosition(0.0f, 3.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        r3 = new Renderer(matCubeMesh, t3);
        // r3.setColors(colors3);
		r3.materials.setMaterial(0, mat3);
        window.addRenderer(r3);
        
        t4 = new Transform();
        t4.setPosition(0.0f, 3.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        r4 = new Renderer(matCubeMesh, t4);
        // r4.setColors(colors3);
		r4.materials.setMaterial(0, mat3);
        window.addRenderer(r4);
        
        int num = 30;
        float rConst = num/(3.141592653f*2f);
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 1;
    		float z = (float)Math.cos(a) * 1;
    		Transform t = new Transform();
    		t.setPosition(x, -2, z);
    		t.setRotation(0, i/(num/360f) + 90f, 0);
    		Renderer r = new Renderer(matCubeMesh, t);
            // r.setColors(colors);
			r.materials.setMaterial(0, mat1);
			window.addRenderer(r);
			boxes.add(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 2;
    		float z = (float)Math.cos(a) * 2;
    		Transform t = new Transform();
    		t.setPosition(x, -1, z);
    		t.setRotation(0, i/(num/360f) + 90f, 0);
    		Renderer r = new Renderer(matCubeMesh, t);
            // r.setColors(colors);
			r.materials.setMaterial(0, mat1);
    		window.addRenderer(r);
			boxes.add(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 3;
    		float z = (float)Math.cos(a) * 3;
    		Transform t = new Transform();
    		t.setPosition(x, 0, z);
    		t.setRotation(0, i/(num/360f) + 90f, 0);
    		Renderer r = new Renderer(matCubeMesh, t);
            // r.setColors(colors);
			r.materials.setMaterial(0, mat1);
    		window.addRenderer(r);
			boxes.add(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 4;
    		float z = (float)Math.cos(a) * 4;
    		Transform t = new Transform();
    		t.setPosition(x, 1, z);
    		t.setRotation(0, i/(num/360f) + 90f, 0);
    		Renderer r = new Renderer(matCubeMesh, t);
            // r.setColors(colors);
			r.materials.setMaterial(0, mat1);
    		window.addRenderer(r);
			boxes.add(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 5;
    		float z = (float)Math.cos(a) * 5;
    		Transform t = new Transform();
    		t.setPosition(x, 2, z);
    		t.setRotation(0, i/(num/360f) + 90f, 0);
    		Renderer r = new Renderer(matCubeMesh, t);
            // r.setColors(colors);
			r.materials.setMaterial(0, mat1);
    		window.addRenderer(r);
			boxes.add(r);
        }
        
//        Transform t2 = new Transform();
//        t2.setPosition(-2.0f, 1.5f, 0.0f);
//        Renderer r2 = new Renderer(matCubeMesh, t2);
//        r2.setShader(shader);
//        renderers.put(1, r2);
        
        lighting.setAbientLighting(Color.decode("0x000000"));
//        lighting.setLightPosition(new Vector3f(0.0f, -1.0f, 0.0f));
//        lighting.setLightColor(Color.decode("0x800000"));
        
        Transform lT = new Transform();
        lT.setPosition(-2.0f, 4.0f, -2.0f);;
        light = new Light(Color.decode("0x800000"),25,lT);
        lighting.addLight(light);

        lighting.setGlobalLightDirection(new Vector3f(0.5f, 0.3f, 0.1f));
		lighting.setGlobalLightColor(Color.decode("0x101010"));
		
		// camera.transform.setPosition(0, 0, 3);
		// camera.transform.setRotation(75, 45, 0);
		camera.recalculateMatrix();
		
		texture = new Texture2D(2, 2);
		texture.setPixel(1, 0, Color.RED);
		texture.setPixel(0, 1, Color.RED);

		material = new Material();
		material.setColor(new Color(0.5f, 0.5f, 0.4f));
		material.setSmoothness(0.5f);
	}
	
	public void run() {
		window.run();
        
        testMesh.dispose();
		matCubeMesh.dispose();
	}
	
	@Override
	public void onLoop() {
		t += window.deltaTime()*45;
		if(t >= 360*2) t = 0;
		// System.out.println(camera.transform);
		// float y = (float)Math.sin(t)*10f;
        // lighting.setLightPosition(new Vector3D(0.0, y, -1.0));
		//
		// colors2[0] = Color.getHSBColor(t/180f, 1, 1);
		// colors3[0] = Color.getHSBColor(t/180f+0.5f, 1, 1);
        //
		// camera.transform.setPosition(0, 0, 6);
		// camera.transform.setRotation(75, t/2f, 0);
        // camera.recalculateMatrix();
        //
        // light.transform.setPosition(0.0f, t/180f, 0.0f);
        // light.setColor(Color.getHSBColor(t/900f, 1, 1));
        //
        // p1.setRotation(0, t, 0);
		// p2.setRotation(0, -t, 0);
		//
		// for (Renderer renderer : boxes) {
		// 	// Vector3f rot = renderer.transform.getRotation();
		// 	renderer.transform.rotate(0, 0, 1);
		// }
	}
	
	@Override
	public void onKeyboardEvent(int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
			// System.out.println("Space was pressed");
			if (testR == null) {
				Transform t = new Transform();
				t.setPosition(0, 2, 0);
				testR = new Renderer(matCubeMesh, t);
				testR.materials.setMaterial(0, material);
				// testR.
				// testR.setColors(colors);
				window.addRenderer(testR);
			} else {
				testR.setVisible(!testR.isVisible());
			}
		} else if (key == GLFW_KEY_LEFT_SHIFT) {
			if (action == GLFW_PRESS) {
				moveMod = true;
			} else if (action == GLFW_RELEASE) {
				moveMod = false;
			}
		}
	}
	@Override
	public void onMouseButtonEvent(int button, int action, int mods) {
		if (button == GLFW_MOUSE_BUTTON_3) {
			if(action == GLFW_PRESS) {
				mouseB3Down = true;
			} else if(action == GLFW_RELEASE) {
				mouseB3Down = false;
			}
		}
	}
	@Override
	public void onMouseCursorEvent(double xPos, double yPos) {
		Vector2d pos = new Vector2d(xPos, yPos);
		if (mouseB3Down) {
			// System.out.println(pos);
			// System.out.println("x: "+xPos+", y="+yPos);
			// Vector2d diff = mouseLPos.sub(sub);
			float dx = (float) (mouseLPos.x - xPos);
			float dy = (float) (mouseLPos.y - yPos);
			// diff.mul(0.01);
			// System.out.println("dx: " + dx + ", dy=" + dy);
			if (moveMod) {
				float z = camera.transform.getPosition().z;
				z += dy / 4f;
				if (z <= 0.5f)
					z = 0.5f;
				if (z > 15)
					z = 15f;
				camera.transform.setPosition(0, 0, z);
				// camera.transform.translate(0f, 0f, dy/4f);
			} else {
				camera.transform.rotate(dy, -dx, 0f);
			}
			camera.recalculateMatrix();
		}
		mouseLPos = pos;
	}
}
