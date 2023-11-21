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
import shaders.Uniform;
import windows.GlfwWindow;

public class Game {

    private Game() {
        throw new IllegalStateException("Game cannot be instantiated. Use static methods");
    }
    
    private static GlfwWindow window;

    /** Game input system */
    public static  InputSystem inputSystem;

    private static  ArrayList<GameObject> gameObjects;
    private static  boolean started = false;

    /** Main camera for game */
    public static  Camera mainCamera;

    private static  SkyBox skybox;

    /** Main shader name in cache */
    public static final String MAIN_SHADER = "main";
    /** Gizmo shader name in cache */
    public static final String GIZMO_SHADER = "gizmo";
    /** Skybox shader name in cache */
    public static final String SKYBOX_SHADER = "skybox";

    /** Game profiler */
    public static  Profiler profiler;

    private static  boolean end = false;

    /** Lighting settings for main shader */
    public static  LightingSettings lightingSettings;

    /** If gizmos should be drawn to the main camera */
    public static  boolean drawGizmos;

    private static  long targetFrameTime = 1000 / 30;
    private static  float targetFPS;
    private static  float lastFrameTime = 0f;
    private static  long lastFrameStart = -1;

    private static  Uniform blendClipUniform;
    private static  Uniform wireUniform;

    // public Game() {
    /**
     * <b>This method must be called before doing anything with the game</b><br>
     * <br>
     * Initializes the game, including the window, input system, camera, etc.<br><br>
     * Also loads default shaders <code>Game.MAIN_SHADER</code>, <code>Game.GIZMO_SHADER</code>, and <code>Game.SKYBOX_SHADER</code>
     */
    public static void init() {
        inputSystem = new InputSystem();

        window = new GlfwWindow("Game");
        mainCamera = new Camera();
        window.setCamera(mainCamera);
        window.addKeyboardListener(inputSystem);
        window.addMouseListener(inputSystem);

        gameObjects = new ArrayList<GameObject>();

        boolean loaded = true;
        loaded = loaded && Shaders.loadShader(GIZMO_SHADER, "shaders/gizmo.fs");
        loaded = loaded && Shaders.loadShader(SKYBOX_SHADER, "shaders/skybox.vs", "shaders/skybox.fs");
        loaded = loaded && Shaders.loadShader(MAIN_SHADER, "shaders/fragment.fs");
        if (!loaded) {
            System.err.println("Could not load main shaders");
        }
        blendClipUniform = new Uniform(Shaders.getMainShader(), "blendClip");

        skybox = new SkyBox();
        skybox.setShader(Shaders.getShader(SKYBOX_SHADER));

        profiler = new Profiler();

        lightingSettings = new LightingSettings(Shaders.getShader(MAIN_SHADER));
		wireUniform = new Uniform(Shaders.getShader(MAIN_SHADER), "wire");
    }

    /**
     * Run the game.<br>
     * <br>
     * Runs <code>MeshCache.dispose()</code> and
     * <code>Shaders.dispose()</code> when done.
     */
    public static void run() {
        window.showWindow(true);

        for (GameObject gameObject : gameObjects) {
            gameObject.onStart();
        }

        lastFrameStart = System.currentTimeMillis();
        end = false;
        while (!end) {
            long cTime = System.currentTimeMillis();
            lastFrameTime = (cTime - lastFrameStart) / 1000f;
            lastFrameStart = cTime;

            profiler.startFrame();
            
            inputSystem.onTick();
            GLFW.glfwPollEvents();
            onTick();

            onRender();

            profiler.start("swap");
            window.swapBuffers();
            profiler.end("swap");

            profiler.endFrame();
            end = end || window.shouldClose();
            if (end)
                continue;

            try {
                Frame f = profiler.getLastFrame();
                long sleepTime = targetFrameTime - f.time();
                if (sleepTime > 2) {
                    Thread.sleep(sleepTime);
                } else {
                    System.err.println("Frame took too long; Target time: "+targetFrameTime+"ms, frame: "+f);
                }
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

    private static  void onTick() {
        profiler.start("tick");

        for (GameObject gameObject : gameObjects) {
            gameObject.onTick();
        }
        profiler.end("tick");
        
        profiler.start("collision");

        for (int i = 0; i < gameObjects.size()-1; i++) {
            GameObject gO = gameObjects.get(i);
            for (int j = i + 1; j < gameObjects.size(); j++) {
                gO.checkCollision(gameObjects.get(j));
            }
            gO.checkCollisionExit();
        }
        gameObjects.get(gameObjects.size()-1).checkCollisionExit();

        profiler.end("collision");
    }

    private static  void onRender() {
        profiler.start("render");

        GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT);

        mainCamera.bindUBO();

        GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_FILL);
        GL45.glEnable(GL45.GL_CULL_FACE);
        GL45.glEnable(GL45.GL_DEPTH_TEST);

        skybox.render();
        GL45.glClear(GL45.GL_DEPTH_BUFFER_BIT);
		wireUniform.setBoolean(false);

        // GL45.glEnable(GL45.GL_)
        GL45.glEnable(GL45.GL_BLEND);
        GL45.glBlendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA);

        blendClipUniform.setFloat(1.0f);
        ArrayList<Renderer> deferredTransparent = new ArrayList<Renderer>();
        ArrayList<Renderer> deferredRender = new ArrayList<Renderer>();
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject.renderer != null) {
                if (gameObject.renderer.defferTransparency) {
                    deferredTransparent.add(gameObject.renderer);
                }
                if (gameObject.renderer.defferRender) {
                    deferredRender.add(gameObject.renderer);
                } else {
                    gameObject.renderer.render();
                }
            }
        }
        blendClipUniform.setFloat(0.0f);
        for (Renderer renderer : deferredTransparent) {
            renderer.render();
        }
        GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_LINE);
        GL45.glDisable(GL45.GL_DEPTH_TEST);
        GL44.glDisable(GL44.GL_CULL_FACE);
		wireUniform.setBoolean(true);
		// unlitUniform.setBoolean(true);
        for (Renderer renderer : deferredRender) {
            renderer.render();
        }
        // GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, GL45.GL_POINT);
        // for (Renderer renderer : deferredRender) {
        //     renderer.render();
        // }
        
        if (drawGizmos) {
			profiler.start("gizmos");
			GL45.glDisable(GL45.GL_DEPTH_TEST);
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
    public static  void addGameObject(GameObject object) {
        gameObjects.add(object);
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
    public static  void deleteGameObject(GameObject object) {
        object.cleanup();
        gameObjects.remove(object);
    }

    /**
     * Set the target frames per second
     * @param fps target frames per second
     */
    public static  void setTargetFPS(float fps) {
        targetFPS = fps;
        targetFrameTime = (long) (1000f / fps);
    }

    /**
     * Get the current target frames per second
     * @return target frames per second
     */
    public static  float getTargetFPS() {
        return targetFPS;
    }
    
    /**
     * Get the time the last frame took to process
     * @return last frame time in seconds
     */
    public static  float deltaTime() {
        return lastFrameTime;
    }

    /**
     * Mark that the game should stop running
     */
    public static  void markEnd() {
        end = true;
    }

    /**
     * Set the window clear color
     * @param color new clear color
     */
    public static  void setClearColor(Color color) {
        GL45.glClearColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f,
                color.getAlpha() / 255f);
    }
}
