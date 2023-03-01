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
	
	public static void main(String[] args) {
		GLFWErrorCallback.createPrint(System.err).set();
		
		GLFW.glfwInit();
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL33.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL33.GL_FALSE);
		
		window = GLFW.glfwCreateWindow(width, height, "OpenGL Testing 2", NULL, NULL);
		GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
		
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
        camera.transform.setPosition(0,0,-3);
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
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f, -1.0f,
        	     0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f, -1.0f, 
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f, -1.0f, 
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f, -1.0f, 
        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f, -1.0f, 
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f, -1.0f, 

        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f,  1.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f,  1.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f,  1.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f,  1.0f,
        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f,  1.0f,
        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  0.0f,  1.0f,

        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,  -1.0f,  0.0f,  0.0f,
        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,  -1.0f,  0.0f,  0.0f,

        	     0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   1.0f,  0.0f,  0.0f,
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   1.0f,  0.0f,  0.0f,
        	     0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   1.0f,  0.0f,  0.0f,
        	     0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   1.0f,  0.0f,  0.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   1.0f,  0.0f,  0.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   1.0f,  0.0f,  0.0f,

        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f, -1.0f,  0.0f,
        	     0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f, -1.0f,  0.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f, -1.0f,  0.0f,
        	     0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f, -1.0f,  0.0f,
        	    -0.5f, -0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f, -1.0f,  0.0f,
        	    -0.5f, -0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f, -1.0f,  0.0f,

        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  1.0f,  0.0f,
        	     0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  1.0f,  0.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  1.0f,  0.0f,
        	     0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  1.0f,  0.0f,
        	    -0.5f,  0.5f,  0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  1.0f,  0.0f,
        	    -0.5f,  0.5f, -0.5f,  0.5f, 0.25f, 0.2f,   0.0f,  1.0f,  0.0f
        };
        testMesh = new Mesh(vertices, true);
        
        renderers = new HashMap<Integer, Renderer>();;
        
        Transform t1 = new Transform();
        t1.setPosition(-1.0f, 0.0f, 0.0f);
        Renderer r1 = new Renderer(testMesh, t1);
        r1.setShader(shader);
        renderers.put(0, r1);
        
        lighting.setAbientLighting(Color.decode("0x505050"));
        lighting.setLightPosition(new Vector3f(-2.0f, 1.0f, -1.0f));
        lighting.setLightColor(Color.decode("0xf000f0"));
        
        GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
        
        while(!GLFW.glfwWindowShouldClose(window)) {
        	GLFW.glfwPollEvents();
    		processInput();
    		shader.bind();
        	loop();
        	GLFW.glfwSwapBuffers(window);
//    		val += 0.001f;
//            GL33.glUniform4f(colorUniform, 0.0f, (float)(Math.sin(val)/2.0)+0.5f, 0.0f, 0.0f);
        }
        
        testMesh.dispose();
        
        GLFW.glfwTerminate();
	}
	
	public static void loop() {
		GL33.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
		
		t += 0.002f;
		if(t >= 360) t = 0;
//		float y = (float)Math.sin(t)*10f;
//        lighting.setLightPosition(new Vector3D(0.0, y, -1.0));
        
        camera.recalculateMatrix();
        
        renderers.get(0).transform.setRotation(0, t, 0);
        
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
