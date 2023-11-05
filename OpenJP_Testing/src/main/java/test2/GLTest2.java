package test2;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

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

public class GLTest2 implements WindowLoopRunnable, Window.KeyboardEventI {

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
	
	Material mat1 = new Material(new Color(0.5f, 0.5f, 0.4f));
	Material mat2 = new Material(new Color(0.0f, 0.75f, 0.4f));
	Material mat3 = new Material(new Color(0.0f, 0.0f, 0.9f));
	
	Window window;
	
	List<Renderer> boxes = new ArrayList<Renderer>();
	Renderer testR;
	Light light;

	Texture2D texture;

	Material material;
    
	public static void main(String[] args) {
		GLTest2 glTest = new GLTest2();
		glTest.run();
	}
	public GLTest2() {
		window = new Window("Open GL Test");
		window.addLoopRunnable(this);
		window.addKeyboardListener(this);
		
		camera = window.camera;
		lighting = window.lightingSettings;
		
        camera.transform.setPosition(0,1,3);
        camera.transform.setRotation(25, -90, 0);
        
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
        Renderer r1 = new Renderer(testMesh, t1);
        // r1.setColors(colors2);
		r1.materials.setMaterial(0, mat2);
        window.addRenderer(r1);
        
        Transform t2 = new Transform();
        t2.setPosition(0.0f, 0.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        Renderer r2 = new Renderer(testMesh, t2);
        // r2.setColors(colors2);
		r2.materials.setMaterial(0, mat2);
        window.addRenderer(r2);
        
        Transform t3 = new Transform();
        t3.setPosition(0.0f, 1.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        Renderer r3 = new Renderer(testMesh, t3);
        // r3.setColors(colors3);
		r3.materials.setMaterial(0, mat3);
        window.addRenderer(r3);
        
        Transform t4 = new Transform();
        t4.setPosition(0.0f, 1.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        Renderer r4 = new Renderer(testMesh, t4);
        // r4.setColors(colors3);
		r4.materials.setMaterial(0, mat3);
        window.addRenderer(r4);
        
        t1 = new Transform();
        t1.setPosition(0.0f, 2.0f, 0.0f);
        t1.setScale(10.0f, 0.1f, 0.1f);
        t1.parent = p1;
        r1 = new Renderer(testMesh, t1);
        // r1.setColors(colors2);
		r1.materials.setMaterial(0, mat2);
        window.addRenderer(r1);
        
        t2 = new Transform();
        t2.setPosition(0.0f, 2.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        r2 = new Renderer(testMesh, t2);
        // r2.setColors(colors2);
		r2.materials.setMaterial(0, mat2);
        window.addRenderer(r2);
        
        t3 = new Transform();
        t3.setPosition(0.0f, 3.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        r3 = new Renderer(testMesh, t3);
        // r3.setColors(colors3);
		r3.materials.setMaterial(0, mat3);
        window.addRenderer(r3);
        
        t4 = new Transform();
        t4.setPosition(0.0f, 3.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        r4 = new Renderer(testMesh, t4);
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
    		Renderer r = new Renderer(testMesh, t);
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
    		Renderer r = new Renderer(testMesh, t);
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
    		Renderer r = new Renderer(testMesh, t);
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
    		Renderer r = new Renderer(testMesh, t);
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
    		Renderer r = new Renderer(testMesh, t);
            // r.setColors(colors);
			r.materials.setMaterial(0, mat1);
    		window.addRenderer(r);
			boxes.add(r);
        }
        
//        Transform t2 = new Transform();
//        t2.setPosition(-2.0f, 1.5f, 0.0f);
//        Renderer r2 = new Renderer(testMesh, t2);
//        r2.setShader(shader);
//        renderers.put(1, r2);
        
        lighting.setAbientLighting(Color.decode("0xf0f0f0"));
//        lighting.setLightPosition(new Vector3f(0.0f, -1.0f, 0.0f));
//        lighting.setLightColor(Color.decode("0x800000"));
        
        Transform lT = new Transform();
        lT.setPosition(0.0f, 2.0f, 0.0f);;
        light = new Light(Color.decode("0x800000"),5,lT);
        lighting.addLight(light);

        lighting.setGlobalLightDirection(new Vector3f(0.5f, 0.3f, 0.1f));
		lighting.setGlobalLightColor(Color.decode("0xf0f0f0"));
		
		camera.transform.setPosition(0, 0, 6);
		camera.transform.setRotation(75, 45, 0);
		camera.recalculateMatrix();
		
		texture = new Texture2D(2, 2);
		texture.setPixel(1, 0, Color.RED);
		texture.setPixel(0, 1, Color.RED);

		material = new Material();
		material.setColor(new Color(0.5f, 0.5f, 0.4f));
		material.setSmoothness(0.9f);
	}
	
	public void run() {
		window.run();
        
        testMesh.dispose();
	}
	
	@Override
	public void onLoop() {
		t += window.deltaTime()*45;
		if(t >= 360*2) t = 0;
//		float y = (float)Math.sin(t)*10f;
//        lighting.setLightPosition(new Vector3D(0.0, y, -1.0));

		// colors2[0] = Color.getHSBColor(t/180f, 1, 1);
		// colors3[0] = Color.getHSBColor(t/180f+0.5f, 1, 1);
        
		// camera.transform.setPosition(0, 0, 6);
		// camera.transform.setRotation(75, t/2f, 0);
        // camera.recalculateMatrix();
        
        // light.transform.setPosition(0.0f, t/180f, 0.0f);
        // light.setColor(Color.getHSBColor(t/900f, 1, 1));
        
        // p1.setRotation(0, t, 0);
		// p2.setRotation(0, -t, 0);
		
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
		}
	}
}
