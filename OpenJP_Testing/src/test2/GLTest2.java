package test2;

import test.ShaderProgram;
import vectorLibrary.Vector3D;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Color;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

public class GLTest2 {

	static ShaderProgram shader;
	static long window;
	
	static int width = 800;
	static int height = 600;
	
	static Mesh testMesh;
	
	static LightingSettings lighting;
	
	public static void main(String[] args) {
		GLFWErrorCallback.createPrint(System.err).set();
		
		glfwInit();
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		
		window = glfwCreateWindow(width, height, "OpenGL Testing 2", NULL, NULL);
		glfwMakeContextCurrent(window);
        GL.createCapabilities();
		
        GL33.glViewport(0,0,width,height);
		glfwSetFramebufferSizeCallback(window, (long window, int width, int height) -> {
			GL33.glViewport(0, 0, width, height);
		});
		
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        
        try {
			shader = new ShaderProgram();
			shader.createVertexShaderFile("src/shaders/vertex.vs");
			shader.createFragmentShaderFile("src/shaders/fragment.fs");
			shader.link();
			shader.bind();
		} catch (Exception e) {
			e.printStackTrace();
            throw new IllegalStateException("Unable to initialize Shader");
		}
        
        lighting = new LightingSettings(shader);
        
        glfwShowWindow(window);
        
//        testMesh = new Mesh(new float[] {
//    		 0.5f,  0.5f, 0.0f,  // top right
//             0.5f, -0.5f, 0.0f,  // bottom right
//            -0.5f, -0.5f, 0.0f,  // bottom left
//            -0.5f,  0.5f, 0.0f   // top left 
//        }, new int[] {
//    		0, 1, 3,  // first Triangle
//            1, 2, 3   // second Triangle
//        });
        testMesh = new Mesh(new float[] {
	    		-0.5f, -0.5f,  0.0f, // left  
	             0.5f, -0.5f,  0.0f, // right 
	             0.0f,  0.5f,  0.0f  // top   
        });
        testMesh.setColors(new float[] {
        		 1.0f, 0.0f, 0.0f,
        		 0.0f, 1.0f, 0.0f,
        		 0.0f, 0.0f, 1.0f,
        		 1.0f, 1.0f, 1.0f
        });
        testMesh.setNormals(new float[] {
        		 0.0f, 0.0f, 1.0f,
        		 0.0f, 0.0f, 1.0f,
        		 0.0f, 0.0f, 1.0f,
        		 0.0f, 0.0f, 1.0f
        });
        
        lighting.setAbientLighting(Color.decode("0x202020"));
        lighting.setLightPosition(new Vector3D(0.0, 0.0, -1.0));
        lighting.setAbientLighting(Color.decode("0xf0f0f0"));
        
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
//        float val = 0.0f;
        
//        int colorUniform = shader.getUniform("testColor");
        
        while(!glfwWindowShouldClose(window)) {
    		glfwPollEvents();
    		processInput();
    		shader.bind();
        	loop();
    		glfwSwapBuffers(window);
//    		val += 0.001f;
//            GL33.glUniform4f(colorUniform, 0.0f, (float)(Math.sin(val)/2.0)+0.5f, 0.0f, 0.0f);
        }
        
        testMesh.dispose();
        
        glfwTerminate();
	}
	
	public static void loop() {
		GL33.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
		
		testMesh.render();
	}
	
	public static void processInput() {
	    if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
	        glfwSetWindowShouldClose(window, true);
	    }
	}
}
