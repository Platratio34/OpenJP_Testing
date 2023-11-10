package test2;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import gizmos.Gizmo;
import gizmos.GizmoType;
import gizmos.Gizmos;
import gizmos.OriginGizmo;
import input.MouseEvent;
import lighting.Light;
import lighting.LightingSettings;
import objects.Camera;
import objects.Mesh;
import objects.MeshRenderer;
import objects.Texture2D;
import objects.Transform;
import shaders.Material;
import util.BinMesh;
import windows.Window;
import windows.WindowLoopRunnable;

public class GLTest2 implements WindowLoopRunnable, MouseEvent {

//	static ShaderProgram shader;
//	static long window;
//	
//	static int width = 800;
//	static int height = 600;
	// Mesh testMesh;
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
	
	List<MeshRenderer> boxes = new ArrayList<MeshRenderer>();
	MeshRenderer testR;
	Light light;

	Texture2D texture;

	private boolean mouseB3Down = false;
	// private boolean moveMod = false;
	private Vector2d mouseLPos = new Vector2d();

	Material material;

	private Vector3f moveDir = new Vector3f();
	private float moveSpeed = 1f;

	private boolean spin;
	private boolean roll;
	private boolean wire;
	private boolean lightA;

	private Mesh testMesh;

	private Transform tMt;
    
	public static void main(String[] args) {
		// try {
		// 	// util.BinMesh.meshResourceToBin("meshes/matCube.mesh", "matCube.bin");
		// 	// util.BinMesh.gizmoResourceToBin("meshes/gizmos/cube.gizmo", "cube.gzb");
		// 	// util.BinMesh.gizmoResourceToBin("meshes/gizmos/origin.gizmo", "origin.gzb");
		// 	// util.BinMesh.gizmoResourceToBin("meshes/gizmos/originX.gizmo", "originX.gzb");
		// 	// util.BinMesh.gizmoResourceToBin("meshes/gizmos/originY.gizmo", "originY.gzb");
		// 	// util.BinMesh.gizmoResourceToBin("meshes/gizmos/originZ.gizmo", "originZ.gzb");
		// 	// util.BinMesh.gizmoResourceToBin("meshes/gizmos/pyramid.gizmo", "pyramid.gzb");
		// } catch (IOException e) {
		// 	e.printStackTrace();
		// }

		GLTest2 glTest = new GLTest2();
		glTest.run();
	}
	public GLTest2() {
		window = new Window("Open GL Test");
		window.addLoopRunnable(this);
		// window.addKeyboardListener(this);
		window.addMouseListener(this);
		
		camera = window.camera;
		lighting = window.lightingSettings;
		
        camera.transform.setPosition(3,5,3);
        camera.transform.setRotation(35, -45, 0);
        
        try {
			// testMesh = Mesh.createFromResource("meshes/newCube.mesh");
			// matCubeMesh = Mesh.createFromResource("meshes/matCube.mesh");
			matCubeMesh = BinMesh.meshFromBinResource("meshes/matCube.bin");
			// testMesh = Mesh.createFromFile("testMesh.mesh");
			testMesh = matCubeMesh;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        
        p1 = new Transform();
		window.addGizmo(new Gizmo(GizmoType.AXIS, Color.white, p1));
        p2 = new Transform();
		window.addGizmo(new Gizmo(GizmoType.AXIS, Color.white, p2));
        
        Transform t1 = new Transform();
        t1.setPosition(0.0f, 0.0f, 0.0f);
        t1.setScale(10.0f, 0.1f, 0.1f);
        t1.parent = p1;
        MeshRenderer r1 = new MeshRenderer(t1, matCubeMesh);
        // r1.setColors(colors2);
		r1.materials.setMaterial(0, mat2);
        window.addRenderer(r1);
        
        Transform t2 = new Transform();
        t2.setPosition(0.0f, 0.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        MeshRenderer r2 = new MeshRenderer(t2, matCubeMesh);
        // r2.setColors(colors2);
		r2.materials.setMaterial(0, mat2);
        window.addRenderer(r2);
        
        Transform t3 = new Transform();
        t3.setPosition(0.0f, 1.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        MeshRenderer r3 = new MeshRenderer(t3, matCubeMesh);
        // r3.setColors(colors3);
		r3.materials.setMaterial(0, mat3);
        window.addRenderer(r3);
        
        Transform t4 = new Transform();
        t4.setPosition(0.0f, 1.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        MeshRenderer r4 = new MeshRenderer(t4, matCubeMesh);
        // r4.setColors(colors3);
		r4.materials.setMaterial(0, mat3);
        window.addRenderer(r4);
        
        t1 = new Transform();
        t1.setPosition(0.0f, 2.0f, 0.0f);
        t1.setScale(10.0f, 0.1f, 0.1f);
        t1.parent = p1;
        r1 = new MeshRenderer(t1, matCubeMesh);
        // r1.setColors(colors2);
		r1.materials.setMaterial(0, mat2);
        window.addRenderer(r1);
        
        t2 = new Transform();
        t2.setPosition(0.0f, 2.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        r2 = new MeshRenderer(t2, matCubeMesh);
        // r2.setColors(colors2);
		r2.materials.setMaterial(0, mat2);
        window.addRenderer(r2);
        
        t3 = new Transform();
        t3.setPosition(0.0f, 3.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        r3 = new MeshRenderer(t3, matCubeMesh);
        // r3.setColors(colors3);
		r3.materials.setMaterial(0, mat3);
        window.addRenderer(r3);
        
        t4 = new Transform();
        t4.setPosition(0.0f, 3.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        r4 = new MeshRenderer(t4, matCubeMesh);
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
    		MeshRenderer r = new MeshRenderer(t, matCubeMesh);
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
    		MeshRenderer r = new MeshRenderer(t, matCubeMesh);
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
    		MeshRenderer r = new MeshRenderer(t, matCubeMesh);
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
    		MeshRenderer r = new MeshRenderer(t, matCubeMesh);
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
    		MeshRenderer r = new MeshRenderer(t, matCubeMesh);
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

		Gizmo g = new Gizmo(GizmoType.PYRAMID, Color.yellow);
		g.setParent(lT);
		g.setSize(0.1f);
		window.addGizmo(g);

		OriginGizmo g2 = new OriginGizmo();
		window.addOriginGizmo(g2);

		// Gizmo cg = new Gizmo(GizmoType.PYRAMID, Color.white);
		// cg.setParent(camera.transform);
		// cg.transform.setScale(1.0f, 1.0f, 1.0f);
		// window.addGizmo(cg);

		// window.setTargetFPS(15);
		tMt = new Transform();
		tMt.setPosition(0.0f, 10.0f, 0.0f);
		MeshRenderer r = new MeshRenderer(tMt, testMesh);
		r.materials.setMaterial(0, mat1);
		window.addRenderer(r);

		window.inputSystem.addBind("spawn", GLFW.GLFW_KEY_SPACE);
		window.inputSystem.addBind("spin", GLFW.GLFW_KEY_1);
		window.inputSystem.addBind("roll", GLFW.GLFW_KEY_2);
		window.inputSystem.addBind("wire", GLFW.GLFW_KEY_3);
		window.inputSystem.addBind("light", GLFW.GLFW_KEY_4);
		window.inputSystem.addBind("gizmo", GLFW.GLFW_KEY_5);
		window.inputSystem.addBind("reload", GLFW.GLFW_KEY_R);
		window.inputSystem.addBind("testMove", GLFW.GLFW_KEY_F);

		window.inputSystem.addAxis("forward/backward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_S);
		window.inputSystem.addAxis("left/right", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D);
		window.inputSystem.addAxis("up/down", GLFW.GLFW_KEY_Q, GLFW.GLFW_KEY_E);
		window.inputSystem.addBind("moveMod", GLFW.GLFW_KEY_LEFT_SHIFT);

		window.drawGizmos = true;
	}
	
	public void run() {
		window.run();
        
        // testMesh.dispose();
		matCubeMesh.dispose();
		Gizmos.dispose();
	}
	
	@Override
	public void onLoop() {
		t += window.deltaTime() * 45;
		if (t >= 360 * 2)
			t = 0;
		
		if (window.inputSystem.pressed("spin")) {
			spin = !spin;
		}
		if (spin) {
			p1.rotate(0f, window.deltaTime() * 45f, 0f);
			p2.rotate(0f, window.deltaTime() * -45f, 0f);
		}

		if (window.inputSystem.pressed("roll")) {
			roll = !roll;
		}
		if (roll) {
			for (MeshRenderer renderer : boxes) {
				renderer.transform.rotate(0, 0, window.deltaTime() * 20f);
			}
		}

		if (window.inputSystem.pressed("wire")) {
			wire = !wire;
			window.setWireframe(wire);
		}

		if (window.inputSystem.pressed("light")) {
			lightA = !lightA;
			if (lightA) {
				lighting.setGlobalLightColor(Color.WHITE);
			} else {
				lighting.setGlobalLightColor(Color.decode("0x101010"));
			}
		}
		
		if (window.inputSystem.pressed("gizmo")) {
			window.drawGizmos = !window.drawGizmos;
		}

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
		moveDir.x = window.inputSystem.axis("forward/backward");
		moveDir.z = window.inputSystem.axis("left/right") * -1f;
		moveDir.y = window.inputSystem.axis("up/down");
		if (moveDir.lengthSquared() > 0) {
			float ms = moveSpeed;
			if (window.inputSystem.down("moveMod")) {
				ms *= 2f;
			}
			camera.transform.translate(camera.transform.forward().mul(moveDir.x * ms * window.deltaTime()));
			camera.transform.translate(camera.transform.right().mul(moveDir.z * ms * window.deltaTime()));
			camera.transform.translate(camera.transform.up().mul(moveDir.y * ms * window.deltaTime()));
			camera.recalculateMatrix();
		}
		
		if (window.inputSystem.pressed("spawn")) {
			if (testR == null) {
				Transform t = new Transform();
				t.setPosition(0, 2, 0);
				testR = new MeshRenderer(t, matCubeMesh);
				testR.materials.setMaterial(0, material);
				// testR.
				// testR.setColors(colors);
				window.addRenderer(testR);
				System.out.println(t.forward());
			} else {
				testR.setVisible(!testR.isVisible());
			}
		}

		if (window.inputSystem.pressed("reload")) {
			try {
				Mesh.createFromFile("testMesh.mesh", testMesh);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (window.inputSystem.pressed("testMove")) {
			tMt.translate(tMt.forward());
		}
	}
	
	@Override
	public void onMouseButtonEvent(int button, int action, int mods) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
			if(action == GLFW.GLFW_PRESS) {
				mouseB3Down = true;
			} else if(action == GLFW.GLFW_RELEASE) {
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
			// if (moveMod) {
			// 	float z = camera.transform.getPosition().z;
			// 	z += dy / 4f;
			// 	if (z <= 0.5f)
			// 		z = 0.5f;
			// 	if (z > 15)
			// 		z = 15f;
			// 	camera.transform.setPosition(0, 0, z);
			// 	// camera.transform.translate(0f, 0f, dy/4f);
			// } else {
				camera.transform.rotate(-dy, -dx, 0);
			// }
			camera.recalculateMatrix();
		}
		mouseLPos = pos;
	}
}
