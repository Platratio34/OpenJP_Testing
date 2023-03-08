package test2;

import java.awt.Color;
import java.io.IOException;

import org.joml.Vector3f;

import lighting.Light;
import lighting.LightingSettings;
import objects.Camera;
import objects.Mesh;
import objects.Renderer;
import objects.Transform;
import windows.Window;
import windows.WindowLoopRunnable;

public class GLTest2 implements WindowLoopRunnable {

//	static ShaderProgram shader;
//	static long window;
//	
//	static int width = 800;
//	static int height = 600;
	
	Mesh testMesh;
	
	LightingSettings lighting;
	
	float t = 0;
	
	Camera camera;
	
//	static HashMap<Integer, Renderer> renderers;
//	protected static int nextId = 0;
	
	Transform p1;
	Transform p2;

    Color[] colors2 = new Color[] {
    	new Color(0.0f, 0.75f, 0.4f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f)
    };
    Color[] colors3 = new Color[] {
    	new Color(0.0f, 0.0f, 0.9f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f),
    	new Color(0.0f, 0.0f, 0.0f)
    };
	
    Window window;
    
	public static void main(String[] args) {
		GLTest2 glTest = new GLTest2();
		glTest.run();
	}
	public GLTest2() {
		window = new Window("Open GL Test");
		window.addLoopRunnable(this);
		
		camera = window.camera;
		lighting = window.lightingSettings;
		
        camera.transform.setPosition(0,1,3);
        camera.transform.setRotation(25, -90, 0);
        
//        testMesh = new Mesh(new float[] {
//             0.5f, -0.5f, -1.5f,  // bottom right
//            -0.5f, -0.5f, -1.5f,  // bottom left
//             0.0f,  0.5f, -1.5f   // top left 
//        });
//        testMesh.setColors(new float[] {
//       		 1.0f, 0.0f, 0.0f,
//       		 0.0f, 1.0f, 0.0f,
//       		 0.0f, 0.0f, 1.0f,
//       		 1.0f, 1.0f, 1.0f
//       });
//       testMesh.setNormals(new float[] {
//       		 0.0f, 0.0f, 1.0f,
//       		 0.0f, 0.0f, 1.0f,
//       		 0.0f, 0.0f, 1.0f,
//       		 0.0f, 0.0f, 1.0f
//       });
        
//        testMesh = Mesh.createFromSingleArray(vertices);
        try {
			testMesh = Mesh.createFromFile("src/meshes/newCube.mesh");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        
        Color[] colors = new Color[] {
        	new Color(0.5f, 0.5f, 0.4f),
        	new Color(0.0f, 0.0f, 0.0f),
        	new Color(0.0f, 0.0f, 0.0f),
        	new Color(0.0f, 0.0f, 0.0f),
        	new Color(0.0f, 0.0f, 0.0f),
        	new Color(0.0f, 0.0f, 0.0f),
        	new Color(0.0f, 0.0f, 0.0f),
        	new Color(0.0f, 0.0f, 0.0f),
        	new Color(0.0f, 0.0f, 0.0f)
        };
        
        p1 = new Transform();
        p2 = new Transform();
        
        Transform t1 = new Transform();
        t1.setPosition(0.0f, 0.0f, 0.0f);
        t1.setScale(10.0f, 0.1f, 0.1f);
        t1.parent = p1;
        Renderer r1 = new Renderer(testMesh, t1);
        r1.setColors(colors2);
        window.addRenderer(r1);
        
        Transform t2 = new Transform();
        t2.setPosition(0.0f, 0.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        Renderer r2 = new Renderer(testMesh, t2);
        r2.setColors(colors2);
        window.addRenderer(r2);
        
        Transform t3 = new Transform();
        t3.setPosition(0.0f, 1.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        Renderer r3 = new Renderer(testMesh, t3);
        r3.setColors(colors3);
        window.addRenderer(r3);
        
        Transform t4 = new Transform();
        t4.setPosition(0.0f, 1.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        Renderer r4 = new Renderer(testMesh, t4);
        r4.setColors(colors3);
        window.addRenderer(r4);
        
        t1 = new Transform();
        t1.setPosition(0.0f, 2.0f, 0.0f);
        t1.setScale(10.0f, 0.1f, 0.1f);
        t1.parent = p1;
        r1 = new Renderer(testMesh, t1);
        r1.setColors(colors2);
        window.addRenderer(r1);
        
        t2 = new Transform();
        t2.setPosition(0.0f, 2.0f, 0.0f);
        t2.setScale(0.1f, 0.1f, 10.0f);
        t2.parent = p1;
        r2 = new Renderer(testMesh, t2);
        r2.setColors(colors2);
        window.addRenderer(r2);
        
        t3 = new Transform();
        t3.setPosition(0.0f, 3.0f, 0.0f);
        t3.setScale(10.0f, 0.1f, 0.1f);
        t3.parent = p2;
        r3 = new Renderer(testMesh, t3);
        r3.setColors(colors3);
        window.addRenderer(r3);
        
        t4 = new Transform();
        t4.setPosition(0.0f, 3.0f, 0.0f);
        t4.setScale(0.1f, 0.1f, 10.0f);
        t4.parent = p2;
        r4 = new Renderer(testMesh, t4);
        r4.setColors(colors3);
        window.addRenderer(r4);
        
        int num = 30;
        float rConst = num/(3.141592653f*2f);
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 1;
    		float z = (float)Math.cos(a) * 1;
    		Transform t = new Transform();
    		t.setPosition(x, -2, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
            r.setColors(colors);
    		window.addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 2;
    		float z = (float)Math.cos(a) * 2;
    		Transform t = new Transform();
    		t.setPosition(x, -1, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
            r.setColors(colors);
    		window.addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 3;
    		float z = (float)Math.cos(a) * 3;
    		Transform t = new Transform();
    		t.setPosition(x, 0, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
            r.setColors(colors);
    		window.addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 4;
    		float z = (float)Math.cos(a) * 4;
    		Transform t = new Transform();
    		t.setPosition(x, 1, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
            r.setColors(colors);
    		window.addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 5;
    		float z = (float)Math.cos(a) * 5;
    		Transform t = new Transform();
    		t.setPosition(x, 2, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
            r.setColors(colors);
    		window.addRenderer(r);
        }
        
//        Transform t2 = new Transform();
//        t2.setPosition(-2.0f, 1.5f, 0.0f);
//        Renderer r2 = new Renderer(testMesh, t2);
//        r2.setShader(shader);
//        renderers.put(1, r2);
        
        lighting.setAbientLighting(Color.decode("0x000040"));
//        lighting.setLightPosition(new Vector3f(0.0f, -1.0f, 0.0f));
//        lighting.setLightColor(Color.decode("0x800000"));
        
        Transform lT = new Transform();
        lT.setPosition(0.0f, -1.0f, 0.0f);;
        Light light = new Light(Color.decode("0x800000"),5,lT);
        lighting.addLight(light);

        lighting.setGlobalLightDirection(new Vector3f(0.5f, 0.3f, 0.1f));
        lighting.setGlobalLightColor(Color.decode("0xf0f0f0"));
	}
	
	public void run() {
		window.run();
        
        testMesh.dispose();
	}
	
	float colorMax = 90f;
	float colorDivMax = 60f;
	
	@Override
	public void onLoop() {
		t += window.deltaTime()*45;
		if(t >= 360*2) t = 0;
//		float y = (float)Math.sin(t)*10f;
//        lighting.setLightPosition(new Vector3D(0.0, y, -1.0));

		colors2[0] = Color.getHSBColor(t/180f, 1, 1);
		colors3[0] = Color.getHSBColor(t/180f+0.5f, 1, 1);
        
		camera.transform.setPosition(0, 0, 6);
		camera.transform.setRotation(75, t/2f, 0);
        camera.recalculateMatrix();
        
        lighting.getLight(0).transform.setPosition(0.0f, t/180f, 0.0f);
        lighting.getLight(0).setColor(Color.getHSBColor(t/900f, 1, 1));
        
        p1.setRotation(0, t, 0);
        p2.setRotation(0, -t, 0);
	}
}
