package windows;

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
import org.lwjgl.opengl.GL44;
import org.lwjgl.opengl.GLUtil;

import gizmos.Gizmo;
import gizmos.OriginGizmo;
import input.InputSystem;
import input.KeyboardEvent;
import input.MouseEvent;
import lighting.LightingSettings;
import objects.Camera;
import objects.Renderer;
import profiling.Profiler;
import shaders.ShaderProgram;
import shaders.SkyBox;
import shaders.Uniform;

/**
 * GLFW OpenGL window
 */
public class Window {
	
	private long window;
	
	private int width = 800;
	private int height = 600;

	/**
	 * Main shader (lit)
	 */
	public ShaderProgram mainShader;
	/**
	 * Main shader lighting settings
	 */
	public LightingSettings lightingSettings;

	/**
	 * Unlit shader
	 */
	public ShaderProgram gizmoShader;

	/**
	 * Skybox shader
	 */
	public ShaderProgram skyboxShader;
	private SkyBox skybox;
	
	/**
	 * Main camera
	 */
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
	/**
	 * If all objects should be rendered unlit
	 */
	public boolean unlitMode = false;
	private Uniform wireUniform;

	/**
	 * Window profiler
	 */
	public Profiler profiler;

	/**
	 * If gizmos should be drawn
	 */
	public boolean drawGizmos;

	/**
	 * Window input system
	 */
	public InputSystem inputSystem;

	/**
	 * Window title. <b>Read Only</b>
	 */
	public String title;
	
	/**
	 * Create a new window and initialize it
	 * @param title
	 */
	public Window(String title) {
		this.title = title;
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
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 5);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL44.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL44.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);

		long mon = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(mon);
		width = vidMode.width();
		height = vidMode.height();
		window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL44.glEnable(GL44.GL_DEPTH_TEST);
		GL44.glEnable(GL44.GL_TEXTURE_2D);

		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
		GLUtil.setupDebugMessageCallback();

		GL44.glViewport(0, 0, width, height);
		GLFW.glfwSetFramebufferSizeCallback(window, (long window, int w, int h) -> {
			width = w;
			height = h;
			GL44.glViewport(0, 0, width, height);
			camera.updateAspectRatio(width, height);
		});

		// GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

		try {
			gizmoShader = new ShaderProgram("gizmo");
			gizmoShader.createVertexShaderResource("shaders/vertex.vs");
			gizmoShader.createFragmentShaderResource("shaders/gizmo.fs");
			gizmoShader.link();
			gizmoShader.bind();

			skyboxShader = new ShaderProgram("skybox");
			skyboxShader.createVertexShaderResource("shaders/vertex.vs");
			skyboxShader.createFragmentShaderResource("shaders/skybox.fs");
			skyboxShader.link();
			skyboxShader.bind();

			mainShader = new ShaderProgram("main");
			mainShader.createVertexShaderResource("shaders/vertex.vs");
			mainShader.createFragmentShaderResource("shaders/fragment.fs");
			mainShader.link();
			mainShader.bind();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Unable to initialize Shaders");
		}

		lightingSettings = new LightingSettings(mainShader);

		camera = new Camera();
		camera.aspectRatio = (float) width / (float) height;
		camera.recalculatePerspective();

		keyboardCallback = new KeyboardCallback();
		GLFW.glfwSetKeyCallback(window, keyboardCallback);
		mouseButtonCallback = new MouseButtonCallback();
		GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback);
		mouseCursorCallback = new MouseCursorCallback();
		GLFW.glfwSetCursorPosCallback(window, mouseCursorCallback);

		// GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);

		unlitUniform = new Uniform(mainShader, "unlit");
		wireUniform = new Uniform(mainShader, "wire");

		GL44.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		inputSystem = new InputSystem();
		addKeyboardListener(inputSystem);

		skybox = new SkyBox();
		skybox.setShader(skyboxShader);
		// addRenderer(sb);
	}
	
	/**
	 * Run the loop until window is closed
	 */
	public void run() {
		showWindow(true);
		// GL44.glPolygonMode(GL44.GL_FRONT_AND_BACK, GL44.GL_LINE);
		lastFrameTime = System.currentTimeMillis();
		while(!GLFW.glfwWindowShouldClose(window)) loop();
        GLFW.glfwTerminate();
	}
	
	private void loop() {
		frameNumber++;
		profiler.startFrame();
		profiler.start("time");
		long time = System.currentTimeMillis();
		frameTime = time - lastFrameTime;
		lastFrameTime = time;
		frameTimes[frameTimeI] = frameTime;
		frameTimeI++;
		if (frameTimeI >= frameTimes.length)
			frameTimeI = 0;
		//		System.out.println("Frame Time: "+frameTime+"ms");
		if (frameNumber > 1 && frameTime > targetFrameTime * 2) {
			long avg = 0;
			for (int i = 0; i < frameTimes.length; i++)
				avg += frameTimes[i];
			avg /= frameTimes.length;
			System.err.println("F-" + frameNumber + "; Frame took " + frameTime + "ms to do; Target frame time: "
					+ targetFrameTime + "ms; Average frame time: " + avg + "ms; " + profiler.getLastFrame(1));
		}
		profiler.end("time");

		profiler.start("input");
		GLFW.glfwPollEvents();
		processInput();
		profiler.end("input");

		profiler.start("init");
		// Uniform objectMatrix = new Uniform(shader, "transformMatrix");
		// Uniform useColor = new Uniform(shader, "useColor");
		GL44.glClear(GL44.GL_COLOR_BUFFER_BIT | GL44.GL_DEPTH_BUFFER_BIT);
		// GL44.glClear(GL44.GL_COLOR_BUFFER_BIT);
		profiler.end("init");

		profiler.start("runnables");
		mainShader.bind();
		for (WindowLoopRunnable runnable : loopRunnables.values()) {
			runnable.onLoop();
		}
		inputSystem.onTick();
		profiler.end("runnables");

		profiler.start("render");
		camera.bindUBO();

		skybox.render();
		mainShader.bind();
		GL44.glEnable(GL44.GL_DEPTH_TEST);

		if (wireframeMode) {
			GL44.glPolygonMode(GL44.GL_FRONT_AND_BACK, GL44.GL_LINE);
			GL44.glDisable(GL44.GL_CULL_FACE);
		} else {
			GL44.glPolygonMode(GL44.GL_FRONT_AND_BACK, GL44.GL_FILL);
			GL44.glEnable(GL44.GL_CULL_FACE);
		}
		wireUniform.setBoolean(wireframeMode);
		unlitUniform.setBoolean(unlitMode);

		for (Renderer renderer : renderers.values()) {
			renderer.render();
		}
		profiler.end("render");

		if (drawGizmos) {
			profiler.start("gizmos");
			gizmoShader.bind();
			GL44.glDisable(GL44.GL_DEPTH_TEST);
			for (Renderer renderer : gizmos.values()) {
				renderer.render();
			}
			profiler.end("gizmos");
		}
		// camera.unbindUBO();

		profiler.start("swap");
		GLFW.glfwSwapBuffers(window);
		profiler.end("swap");

		profiler.endFrame();

		long eTime = System.currentTimeMillis();
		// long pTime = eTime - time;
		// if(pTime > targetFrameTime*1.5f) {
		// 	System.err.println("F-"+frameNumber+"; "+profiler.getLastFrame());
		// }
		// System.out.println(eTime - time);

		try {
			long sleepTime = targetFrameTime - (eTime - lastFrameTime);
			//    		System.out.println("- Sleep time: "+sleepTime+"ms");
			if (sleepTime > 2)
				Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set window visibility
	 * @param show window visibility
	 */
	public void showWindow(boolean show) {
		if(show) {
			GLFW.glfwShowWindow(window);
		} else {
			GLFW.glfwHideWindow(window);
		}
	}
	
	private void processInput() {
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
			GLFW.glfwSetWindowShouldClose(window, true);
		}
	}
	
	/**
	 * Add a renderer to be drawn in the window
	 * @param renderer renderer to add
	 * @return renderer id
	 */
	public int addRenderer(Renderer renderer) {
		renderer.setShader(mainShader);
		while (renderers.containsKey(nextIdRend)) {
			nextIdRend++;
		}
		renderers.put(nextIdRend, renderer);
		nextIdRend++;
		return nextIdRend - 1;
	}

	/**
	 * Add a gizmo renderer to be drawn in the window
	 * @param renderer gizmo renderer to add
	 * @return gizmo id
	 */
	public int addGizmo(Renderer renderer) {
		renderer.setShader(gizmoShader);
		while (gizmos.containsKey(nextIdGizmo)) {
			nextIdGizmo++;
		}
		gizmos.put(nextIdGizmo, renderer);
		nextIdGizmo++;
		return nextIdGizmo - 1;
	}
	/**
	 * Add a gizmo to be drawn in the window
	 * @param gizmo gizmo to add
	 * @return gizmo id
	 */
	public int addGizmo(Gizmo gizmo) {
		return addGizmo(gizmo.renderer);
	}
	/**
	 * Add an origin gizmo to be drawn in window
	 * @param gizmo origin gizmo to add
	 */
	public void addOriginGizmo(OriginGizmo gizmo) {
		// System.out.println("Adding origin gizmo");
		addGizmo(gizmo.renderer);
		addGizmo(gizmo.getYRenderer());
		addGizmo(gizmo.getZRenderer());
	}

	/**
	 * Add an on-tick runnable
	 * @param loopRunnable runnable to add
	 * @return runnable id
	 */
	public int addLoopRunnable(WindowLoopRunnable loopRunnable) {
		while (renderers.containsKey(nextIdLR)) {
			nextIdLR++;
		}
		loopRunnables.put(nextIdRend, loopRunnable);
		nextIdLR++;
		return nextIdLR - 1;
	}
	
	/**
	 * Time since last frame in seconds
	 * @return last frame time in seconds
	 */
	public float deltaTime() {
		return frameTime / 1000f;
	}
	
	/**
	 * Set the target frame rate
	 * @param fps target frames per second
	 */
	public void setTargetFPS(int fps) {
		targetFrameTime = 1000 / fps;
	}

	/**
	 * Set if renderers should be drawn in wireframe
	 * @param wire draw in wireframe
	 */
	public void setWireframe(boolean wire) {
		wireframeMode = wire;
	}

	/**
	 * Add a keyboard listener
	 * @param listener keyboard listener
	 */
	public void addKeyboardListener(KeyboardEvent listener) {
		keyboardCallback.listeners.add(listener);
	}

	private class KeyboardCallback implements GLFWKeyCallbackI {

		ArrayList<KeyboardEvent> listeners;

		KeyboardCallback() {
			listeners = new ArrayList<KeyboardEvent>();
		}

		@Override
		public void invoke(long window, int key, int scanCode, int action, int mods) {
			for (KeyboardEvent listener : listeners) {
				listener.onKeyboardEvent(key, scanCode, action, mods);
			}
		}

	}
	
	
	/**
	 * Add a mouse button and position listener
	 * @param listener mouse listener
	 */
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
