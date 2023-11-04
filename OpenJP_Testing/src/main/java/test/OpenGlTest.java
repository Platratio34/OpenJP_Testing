package test;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import oldObjects.Cube;
import oldObjects.GL_Drawable;
import oldObjects.Plane;
import oldObjects.Triangle;
// import vectorLibrary.Vector2D;
import org.joml.Vector2f;
// import vectorLibrary.Vector3D;
import org.joml.Vector3f;

public class OpenGlTest {
	
	// The window handle
    private long window;
    private ArrayList<GL_Drawable> drawables;
    private Vector3f cPos;
    private double dir = 0.1;
    
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        
        try {
            init();
            loop();

            // Release window and window callbacks
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void init() {
    	
    	drawables = new ArrayList<GL_Drawable>();
    	cPos = new Vector3f(0,0,-30);
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window will be resizable

        int WIDTH = 800;
        int HEIGHT = 800;

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Spinny Boxes!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - WIDTH) / 2,
                (vidmode.height() - HEIGHT) / 2
        );
        
        drawables.add(new Plane(new Vector2f(30,30), new Vector3f(0,-5.1f,0), new Vector3f(0,0,0), new Vector3f(0.1f,0.5f,0.05f)));
        drawables.add(new Plane(new Vector2f(10,10), new Vector3f(0,-5,0), new Vector3f(0,0,0), new Vector3f(0.2f,0.2f,0.2f)));
        
        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,-3,-3), new Vector3f(0,0,0), 1));
        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,3,-3), new Vector3f(0,0,0), 1));
        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,-3,-3), new Vector3f(0,0,0), 1));
        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,3,-3), new Vector3f(0,0,0), 1));

        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,-3,3), new Vector3f(0,0,0), 1f));
        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(-3,3,3), new Vector3f(0,0,0), 1f));
        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,-3,3), new Vector3f(0,0,0), 1f));
        drawables.add(new Cube(new Vector3f(2,2,2), new Vector3f(3,3,3), new Vector3f(0,0,0), 1f));
        
        Triangle[] cubeMesh = new Triangle[0];
        Triangle[] pyramidMesh = new Triangle[0];
        try {
        	cubeMesh = Triangle.loadMesh("mehses/cube.mesh");
        	pyramidMesh = Triangle.loadMesh("mehses/pyramid.mesh");
        	System.out.println("Test 2");
		} catch (IOException e) {
			e.printStackTrace();
		}
//        drawables.add(new Mesh(new Vector3f(-3,-3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
//        drawables.add(new Mesh(new Vector3f(-3,3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
//        drawables.add(new Mesh(new Vector3f(3,-3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
//        drawables.add(new Mesh(new Vector3f(3,3,-3), new Vector3f(0,0,0), new Vector3f(2,2,2), cubeMesh));
//
//        drawables.add(new Mesh(new Vector3f(-3,-3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh));
//        drawables.add(new Mesh(new Vector3f(-3,3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh));
//        drawables.add(new Mesh(new Vector3f(3,-3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh));
//        drawables.add(new Mesh(new Vector3f(3,3,3), new Vector3f(0,0,0), new Vector3f(2,2,2), pyramidMesh));
//        drawables.add(new Cube(new Vector3f(1,1,1), new Vector3f(0,0,1), new Vector3f(0,0,0)));
        
        
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.01f, 0.9f, 0.9f, 0.0f);
        glEnable(GL_DEPTH_TEST);
//        glDepthRange(0.01, 500);
//        glFrustum();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        float pitch = 0;
        float yaw = 0;
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            
//            cPos.z += dir;
//            if(cPos.z >= 20) {
//            	dir = -0.1;
//            }
//            if(cPos.z <= -20) {
//            	dir = 0.1;
//            }
            cPos.z = 0;
            
            pitch += 0.3f;
            pitch %= 360f;
            
            glLoadIdentity();
            for(GL_Drawable d : drawables) {
	            d.draw(cPos, new Vector3f(-45, pitch, 0), new Vector3f(45,0,0));
            }

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
//    	OpenGlTest t = new OpenGlTest();
//        try {
//	        t.run();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

    	TestWindow t = new TestWindow();
    	try {
	        t.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
