package windows;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.HashMap;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import gizmos.Gizmo;
import input.InputSystem;
import input.KeyboardEvent;
import input.MouseEvent;
import lighting.LightingSettings;
import objects.Camera;
import objects.Renderer;
import profileing.Profiler;
import shaders.ShaderProgram;
import shaders.Uniform;

public class Window {
	
	private long window;
	
	private int width = 800;
	private int height = 600;

	public ShaderProgram shader;
	public LightingSettings lightingSettings;
	
	public Camera camera;
	
	private HashMap<Integer, Renderer> renderers;
	private int nextIdRend = 0;
	private HashMap<Integer, Renderer> gizmos;
	private int nextIdGizmo = 0;

	private HashMap<Integer, WindowLoopRunnable> loopRunnables;
	private int nextIdLR = 0;
	
	private long lastFrameTime;
	private long frameTime;
	private long targetFrameTime = 1000/30;
	private long frameNumber;
	private long[] frameTimes = new long[10];
	private int frameTimeI = 0;

	private KeyboardCallback keyboardCallback;
	private MouseButtonCallback mouseButtonCallback;
	private MouseCursorCallback mouseCursorCallback;

	private Uniform unlitUniform;

	private boolean wireframeMode = false;

	public Profiler profiler;

	public boolean drawGizmos;

	public InputSystem inputSystem;
	
	public Window(String title) {
		init();
	}
	
	private void init() {
		renderers = new HashMap<Integer, Renderer>();
		gizmos = new HashMap<Integer, Renderer>();
		loopRunnables = new HashMap<Integer, WindowLoopRunnable>();
		profiler = new Profiler();
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		GLFW.glfwInit();
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL33.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL33.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
		
		long mon = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(mon);
		width = vidmode.width();
		height = vidmode.height();
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
		
		
        // GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        
        try {
			shader = new ShaderProgram();
			shader.createVertexShaderResource("shaders/vertex.vs");
			shader.createFragmentShaderResource("shaders/fragment.fs");
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


		keyboardCallback = new KeyboardCallback();
		glfwSetKeyCallback(window, keyboardCallback);
		mouseButtonCallback = new MouseButtonCallback();
		glfwSetMouseButtonCallback(window, mouseButtonCallback);
		mouseCursorCallback = new MouseCursorCallback();
		glfwSetCursorPosCallback(window, mouseCursorCallback);

		unlitUniform = new Uniform(shader, "unlit");

		GL33.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		inputSystem = new InputSystem();
		addKeyboardListener(inputSystem);
	}
	
	public void run() {
		showWindow(true);
		// GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
		lastFrameTime = System.currentTimeMillis();
		while(!GLFW.glfwWindowShouldClose(window)) loop();
        GLFW.glfwTerminate();
	}
	
	public void loop() {
		frameNumber++;
		profiler.startFrame();
		profiler.start("time");
		long time = System.currentTimeMillis();
		frameTime = time - lastFrameTime;
		lastFrameTime = time;
		frameTimes[frameTimeI] = frameTime;
		frameTimeI++;
		if(frameTimeI >= frameTimes.length) frameTimeI = 0;
//		System.out.println("Frame Time: "+frameTime+"ms");
		if(frameTime > targetFrameTime * 2) {
			long avg = 0;
			for(int i = 0; i < frameTimes.length; i++) avg += frameTimes[i];
			avg /= frameTimes.length;
			System.err.println("F-"+frameNumber+"; Frame took "+frameTime+"ms to do; Target frame time: "+targetFrameTime+"ms; Average frame time: "+avg+"ms; "+profiler.getLastFrame(1));
		}
		profiler.end("time");

		profiler.start("input");
		GLFW.glfwPollEvents();
		processInput();
		profiler.end("input");

		profiler.start("init");
		shader.bind();
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
		// GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
		profiler.end("init");
		
		profiler.start("runnables");
		for (WindowLoopRunnable runnable : loopRunnables.values()) {
			runnable.onLoop();
		}
		inputSystem.onTick();
		profiler.end("runnables");
		
		profiler.start("render");
        GL33.glEnable(GL33.GL_DEPTH_TEST);
		
		if (wireframeMode) {
			GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
			GL33.glDisable(GL33.GL_CULL_FACE);
		} else {
			GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_FILL);
			GL33.glEnable(GL33.GL_CULL_FACE);
		}
		unlitUniform.setBoolean(wireframeMode);

		for (Renderer renderer : renderers.values()) {
			renderer.render();
		}
		profiler.end("render");

		if(drawGizmos) {
			profiler.start("gizmos");
			// GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
			unlitUniform.setBoolean(true);
			GL33.glDisable(GL33.GL_DEPTH_TEST);
			// GL33.glDisable(GL33.GL_CULL_FACE);
			for (Renderer renderer : gizmos.values()) {
				renderer.render();
			}
			profiler.end("gizmos");
		}
    	
		profiler.start("swap");
    	GLFW.glfwSwapBuffers(window);
		profiler.end("swap");

		profiler.endFrame();

		long eTime = System.currentTimeMillis();
		long pTime = eTime - time;
		if(pTime > targetFrameTime) {
			System.err.println("F-"+frameNumber+"; "+profiler.getLastFrame());
		}
		// System.out.println(eTime - time);
    	
    	try {
    		long sleepTime = targetFrameTime - (eTime - lastFrameTime);
//    		System.out.println("- Sleep time: "+sleepTime+"ms");
    		if(sleepTime > 2) Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		while (renderers.containsKey(nextIdRend)) {
			nextIdRend++;
		}
		renderers.put(nextIdRend, renderer);
		nextIdRend++;
		return nextIdRend - 1;
	}
	public int addGizmo(Renderer renderer) {
		renderer.setShader(shader);
		while(gizmos.containsKey(nextIdGizmo)) {
			nextIdGizmo++;
		}
		gizmos.put(nextIdGizmo, renderer);
		nextIdGizmo++;
		return nextIdGizmo-1;
	}
	public int addGizmo(Gizmo gizmo) {
		return addGizmo(gizmo.renderer);
	}

	public int addLoopRunnable(WindowLoopRunnable loopRunnable) {
		while(renderers.containsKey(nextIdLR)) {
			nextIdLR++;
		}
		loopRunnables.put(nextIdRend, loopRunnable);
		nextIdLR++;
		return nextIdLR-1;
	}
	
	public float deltaTime() {
		return frameTime/1000f;
	}
	
	public void setTargetFPS(int fps) {
		targetFrameTime = 1000 / fps;
	}

	public void setWireframe(boolean wire) {
		wireframeMode = wire;
	}

	public void addKeyboardListener(KeyboardEvent listener) {
		keyboardCallback.listeners.add(listener);
	}

	private class KeyboardCallback implements GLFWKeyCallbackI {

		ArrayList<KeyboardEvent> listeners;

		KeyboardCallback() {
			listeners = new ArrayList<KeyboardEvent>();
		}

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			for (KeyboardEvent listener : listeners) {
				listener.onKeyboardEvent(key, scancode, action, mods);
			}
		}

	}
	

	public void addMouseListener(MouseEvent listener) {
		mouseButtonCallback.listeners.add(listener);
		mouseCursorCallback.listeners.add(listener);
	}

	private class MouseButtonCallback implements GLFWMouseButtonCallbackI {

		ArrayList<MouseEvent> listeners;

		MouseButtonCallback() {
			listeners = new ArrayList<MouseEvent>();
		}

		@Override
		public void invoke(long window, int button, int action, int mods) {
			for (MouseEvent listener : listeners) {
				listener.onMouseButtonEvent(button, action, mods);
			}
		}

	}
	private class MouseCursorCallback implements GLFWCursorPosCallbackI {

		ArrayList<MouseEvent> listeners;

		MouseCursorCallback() {
			listeners = new ArrayList<MouseEvent>();
		}

		@Override
		public void invoke(long window, double xPos, double yPos) {
			for (MouseEvent listener : listeners) {
				listener.onMouseCursorEvent(xPos, yPos);
			}
		}

	}
	
}
