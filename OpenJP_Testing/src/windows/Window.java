package windows;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import lighting.LightingSettings;
import objects.Camera;
import objects.Renderer;
import shaders.ShaderProgram;

public class Window {
	
	private long window;
	
	private int width = 800;
	private int height = 600;

	public ShaderProgram shader;
	public LightingSettings lightingSettings;
	
	public Camera camera;
	
	private HashMap<Integer, Renderer> renderers;
	private int nextIdRend = 0;

	private HashMap<Integer, WindowLoopRunnable> loopRunnables;
	private int nextIdLR = 0;
	
	public Window(String title) {
		init();
	}
	
	private void init() {
		renderers = new HashMap<Integer, Renderer>();
		loopRunnables = new HashMap<Integer, WindowLoopRunnable>();
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		GLFW.glfwInit();
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL33.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL33.GL_TRUE);
		
		window = GLFW.glfwCreateWindow(width, height, "OpenGL Testing 2", NULL, NULL);
		GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL33.glEnable(GL33.GL_DEPTH_TEST);
		
        GL33.glViewport(0,0,width,height);
        GLFW.glfwSetFramebufferSizeCallback(window, (long window, int w, int h) -> {
			width = w;
			height = h;
			GL33.glViewport(0, 0, width, height);
			camera.updateAspectRation(width, height);
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
        
        lightingSettings = new LightingSettings(shader);
        
        camera = new Camera(shader);
		camera.aspectRatio = (float)width/(float)height;
		camera.recaculatePerspective();

		GL33.glEnable(GL33.GL_CULL_FACE);
	}
	
	public void run() {
		showWindow(true);
//		GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
		while(!GLFW.glfwWindowShouldClose(window)) loop();
        GLFW.glfwTerminate();
	}
	
	public void loop() {
		GLFW.glfwPollEvents();
		processInput();
		shader.bind();
		GL33.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
		
		for (WindowLoopRunnable runnable : loopRunnables.values()) {
			runnable.onLoop();
		}
		
		for (Renderer renderer : renderers.values()) {
        	renderer.render();
		}
    	
    	GLFW.glfwSwapBuffers(window);
	}
	
	public void showWindow(boolean show) {
		if(show) {
			GLFW.glfwShowWindow(window);
		} else {
			GLFW.glfwHideWindow(window);
		}
	}
	
	private void processInput() {
	    if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
	    	GLFW.glfwSetWindowShouldClose(window, true);
	    }
	}
	
	public int addRenderer(Renderer renderer) {
		renderer.setShader(shader);
		while(renderers.containsKey(nextIdRend)) {
			nextIdRend++;
		}
		renderers.put(nextIdRend, renderer);
		nextIdRend++;
		return nextIdRend-1;
	}

	public int addLoopRunnable(WindowLoopRunnable loopRunnable) {
		while(renderers.containsKey(nextIdLR)) {
			nextIdLR++;
		}
		loopRunnables.put(nextIdRend, loopRunnable);
		nextIdLR++;
		return nextIdLR-1;
	}
	
}
