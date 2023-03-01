package test2;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Color;
import java.util.HashMap;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import test.ShaderProgram;
import vectorLibrary.Vector3D;

public class GLTest2 {

	static ShaderProgram shader;
	static long window;
	
	static int width = 800;
	static int height = 600;
	
	static Mesh testMesh;
	
	static LightingSettings lighting;
	
	static float t = 0;
	
	static Camera camera;
	
	static HashMap<Integer, Renderer> renderers;
	protected static int nextId = 0;
	
	public static void main(String[] args) {
		GLFWErrorCallback.createPrint(System.err).set();
		
		GLFW.glfwInit();
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL33.GL_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL33.GL_FALSE);
		
		window = GLFW.glfwCreateWindow(width, height, "OpenGL Testing 2", NULL, NULL);
		GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL33.glEnable(GL33.GL_DEPTH_TEST);
		
        GL33.glViewport(0,0,width,height);
        GLFW.glfwSetFramebufferSizeCallback(window, (long window, int w, int h) -> {
			width = w;
			height = h;
			GL33.glViewport(0, 0, width, height);
			camera.aspectRatio = (float)width/(float)height;
			camera.recaculatePerspective();
		});
		
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        
        try {
			shader = new ShaderProgram();
			shader.createVertexShaderFile("src/shaders/vertex.vert");
			shader.createFragmentShaderFile("src/shaders/fragment.frag");
			shader.link();
			shader.bind();
		} catch (Exception e) {
			e.printStackTrace();
            throw new IllegalStateException("Unable to initialize Shader");
		}
        
        lighting = new LightingSettings(shader);
        
        camera = new Camera(shader);
        camera.transform.setPosition(0,1,3);
        camera.transform.setRotation(25, -90, 0);
		camera.aspectRatio = (float)width/(float)height;
		camera.recaculatePerspective();
        
        GLFW.glfwShowWindow(window);
        
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
       float vertices[] = {
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f, -1.0f,
        	     0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f, -1.0f, 
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f, -1.0f, 
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f, -1.0f, 
        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f, -1.0f, 
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f, -1.0f, 

        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f,  1.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f,  1.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f,  1.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f,  1.0f,
        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f,  1.0f,
        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  0.0f,  1.0f,

        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,  -1.0f,  0.0f,  0.0f,

        	     0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   1.0f,  0.0f,  0.0f,
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   1.0f,  0.0f,  0.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   1.0f,  0.0f,  0.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   1.0f,  0.0f,  0.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   1.0f,  0.0f,  0.0f,
        	     0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   1.0f,  0.0f,  0.0f,

        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f, -1.0f,  0.0f,
        	     0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f, -1.0f,  0.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f, -1.0f,  0.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f, -1.0f,  0.0f,
        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f, -1.0f,  0.0f,
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f, -1.0f,  0.0f,

        	     0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  1.0f,  0.0f,
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  1.0f,  0.0f,
        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  1.0f,  0.0f,
        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  1.0f,  0.0f,
        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  1.0f,  0.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.4f,   0.0f,  1.0f,  0.0f
        };
        testMesh = new Mesh(vertices, true);
        
        renderers = new HashMap<Integer, Renderer>();;
        
//        Transform t1 = new Transform();
//        t1.setPosition(-2.0f, 0.0f, 0.0f);
//        Renderer r1 = new Renderer(testMesh, t1);
//        r1.setShader(shader);
//        renderers.put(0, r1);
        
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
    		r.setShader(shader);
    		addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 2;
    		float z = (float)Math.cos(a) * 2;
    		Transform t = new Transform();
    		t.setPosition(x, -1, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
    		r.setShader(shader);
    		addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 3;
    		float z = (float)Math.cos(a) * 3;
    		Transform t = new Transform();
    		t.setPosition(x, 0, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
    		r.setShader(shader);
    		addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 4;
    		float z = (float)Math.cos(a) * 4;
    		Transform t = new Transform();
    		t.setPosition(x, 1, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
    		r.setShader(shader);
    		addRenderer(r);
        }
        for(int i = 0; i < num; i++) {
        	float a = i/rConst;
    		float x = (float)Math.sin(a) * 5;
    		float z = (float)Math.cos(a) * 5;
    		Transform t = new Transform();
    		t.setPosition(x, 2, z);
    		t.setRotation(0, i/(num/360f), 0);
    		Renderer r = new Renderer(testMesh, t);
    		r.setShader(shader);
    		addRenderer(r);
        }
        
//        Transform t2 = new Transform();
//        t2.setPosition(-2.0f, 1.5f, 0.0f);
//        Renderer r2 = new Renderer(testMesh, t2);
//        r2.setShader(shader);
//        renderers.put(1, r2);
        
        lighting.setAbientLighting(Color.decode("0x000040"));
        lighting.setLightPosition(new Vector3f(0.0f, -1.0f, 0.0f));
        lighting.setLightColor(Color.decode("0x800000"));

        lighting.setGlobalLightDirection(new Vector3f(0.5f, 0.3f, 0.1f));
        lighting.setGlobalLightColor(Color.decode("0xf0f0f0"));
        
//        GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
        GL33.glEnable(GL33.GL_CULL_FACE);
        
        while(!GLFW.glfwWindowShouldClose(window)) {
        	GLFW.glfwPollEvents();
    		processInput();
    		shader.bind();
    		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
        	loop();
        	GLFW.glfwSwapBuffers(window);
//    		val += 0.001f;
//            GL33.glUniform4f(colorUniform, 0.0f, (float)(Math.sin(val)/2.0)+0.5f, 0.0f, 0.0f);
        }
        
        testMesh.dispose();
        
        GLFW.glfwTerminate();
	}
	
	public static int addRenderer(Renderer renderer) {
		while(renderers.containsKey(nextId)) {
			nextId++;
		}
		renderers.put(nextId, renderer);
		nextId++;
		return nextId-1;
	}
	
	public static void loop() {
		GL33.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
		
		t += 0.2f;
		if(t >= 360) t = 0;
//		float y = (float)Math.sin(t)*10f;
//        lighting.setLightPosition(new Vector3D(0.0, y, -1.0));
        
		camera.transform.setRotation(20, t, 0);
        camera.recalculateMatrix();
        
//        renderers.get(0).transform.setRotation(0, t, 0);
        
        for (Renderer renderer : renderers.values()) {
        	renderer.render();
		}
		
		
	}
	
	public static void processInput() {
	    if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
	    	GLFW.glfwSetWindowShouldClose(window, true);
	    }
	}
}
