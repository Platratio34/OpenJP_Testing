package game;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL44;
import org.lwjgl.opengl.GL45;

import java.awt.Color;

import input.InputSystem;
import lighting.LightingSettings;
import objects.Camera;
import objects.MeshCache;
import objects.Renderer;
import profiling.Frame;
import profiling.Profiler;
import shaders.Shaders;
import shaders.SkyBox;
import windows.GlfwWindow;

public class Game {
    
    private GlfwWindow window;

    /** Game input system */
    public InputSystem inputSystem;

    private ArrayList<GameObject> gameObjects;
    private boolean started = false;

    /** Main camera for game */
    public Camera mainCamera;

    private Color clearColor = new Color(0, 0, 0, 1);

    private SkyBox skybox;

    /** Main shader name in cache */
    public static final String MAIN_SHADER = "main";
    /** Gizmo shader name in cache */
    public static final String GIZMO_SHADER = "gizmo";
    /** Skybox shader name in cache */
    public static final String SKYBOX_SHADER = "skybox";

    /** Game profiler */
    public Profiler profiler;

    private boolean end = false;

    /** Lighting settings for main shader */
    public LightingSettings lightingSettings;

    /** If gizmos should be drawn to the main camera */
    public boolean drawGizmos;

    private long targetFrameTime = 1000 / 30;
    private float targetFPS;
    private float lastFrameTime = 0f;

    public Game() {
        mainCamera = new Camera();
        window = new GlfwWindow("Game", mainCamera);
        window.addKeyboardListener(inputSystem);

        gameObjects = new ArrayList<GameObject>();

        boolean loaded = true;
        loaded = loaded && Shaders.loadShader(MAIN_SHADER, "shaders/fragment.fs");
        loaded = loaded && Shaders.loadShader(GIZMO_SHADER, "shaders/skybox.fs");
        loaded = loaded && Shaders.loadShader(SKYBOX_SHADER, "shaders/gizmo.fs");
        if (!loaded) {
            System.err.println("Could not load main shaders");
        }

        skybox = new SkyBox();
        skybox.setShader(Shaders.getShader(SKYBOX_SHADER));

        profiler = new Profiler();

        lightingSettings = new LightingSettings(Shaders.getShader(MAIN_SHADER));
    }

    /**
     * Run the game.<br>
     * <br>
     * Runs <code>MeshCache.dispose()</code> and
     * <code>Shaders.dispose()</code> when done.
     */
    public void run() {
        window.showWindow(true);

        for (GameObject gameObject : gameObjects) {
            gameObject.onStart();
        }

        end = false;
        while (!end) {
            profiler.startFrame();

            GLFW.glfwPollEvents();
            onTick();

            onRender();
            profiler.start("swap");
            window.swapBuffers();
            profiler.end("swap");

            profiler.endFrame();
            end = end || window.shouldClose();
            if(end) continue;

            try {
                Frame f = profiler.getLastFrame();
                long sleepTime = targetFrameTime - f.time();
                lastFrameTime = f.time() / 1000f;
                if (sleepTime > 2)
                    Thread.sleep(sleepTime);
                else
                    System.err.println("Frame took too long; Target time: "+targetFrameTime+"ms, frame: "+f);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (GameObject gameObject : gameObjects) {
            gameObject.cleanup();
        }

        GLFW.glfwTerminate();

        MeshCache.dispose();
        Shaders.dispose();
    }

    private void onTick() {
        profiler.start("tick");

        for (GameObject gameObject : gameObjects) {
            gameObject.onTick();
        }

        inputSystem.onTick();
        profiler.end("tick");
    }

    private void onRender() {
        profiler.start("render");
        GL45.glClearColor(clearColor.getRed() / 255f, clearColor.getGreen() / 255f, clearColor.getBlue() / 255f,
                clearColor.getAlpha() / 255f);
        GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT);

        mainCamera.bindUBO();

        skybox.render();
        GL45.glEnable(GL45.GL_DEPTH_TEST);

        for (GameObject gameObject : gameObjects) {
            if (gameObject.renderer != null)
                gameObject.renderer.render();
        }
        
        if (drawGizmos) {
			profiler.start("gizmos");
			GL44.glDisable(GL44.GL_DEPTH_TEST);
			for (GameObject gameObject : gameObjects) {
                if (gameObject.gizmo != null)
                    gameObject.gizmo.render();
            }
			profiler.end("gizmos");
		}

        profiler.end("render");
    }

    /**
     * Add a object to the game.
     * Will run <code>onStart()</code> if added after game is started
     * @param object game object to add
     */
    public void addGameObject(GameObject object) {
        gameObjects.add(object);
        object.inputSystem = inputSystem;
        object.game = this;
        if (started) {
            object.onStart();
        }
    }

    /**
     * Delete a game object from the game.<br>
     * <br>
     * <b>Calls <code>cleanup()</code> on the object</b>
     * @param object game object to delete
     */
    public void deleteGameObject(GameObject object) {
        object.cleanup();
        gameObjects.remove(object);
    }

    /**
     * Set the target frames per second
     * @param fps target frames per second
     */
    public void setTargetFPS(float fps) {
        targetFPS = fps;
        targetFrameTime = (long) (1000f / fps);
    }
    
    /**
     * Get the time the last frame took to process
     * @return last frame time in seconds
     */
    public float deltaTime() {
        return lastFrameTime;
    }

    public void markEnd() {
        end = true;
    }
}