package windows;

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
import static org.lwjgl.system.MemoryUtil.NULL;

import input.KeyboardEvent;
import input.MouseEvent;
import objects.Camera;

public class GlfwWindow {
    
    private long windowId;

    private int width;
    private int height;

    private KeyboardCallback keyboardCallback;
	private MouseButtonCallback mouseButtonCallback;
    private MouseCursorCallback mouseCursorCallback;
    
    private String title;

    public GlfwWindow(String title, Camera camera) {
        this.title = title;

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
        windowId = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        GLFW.glfwMakeContextCurrent(windowId);
        GL.createCapabilities();
        GL44.glEnable(GL44.GL_DEPTH_TEST);
        GL44.glEnable(GL44.GL_TEXTURE_2D);

        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
        GLUtil.setupDebugMessageCallback();

        GL44.glViewport(0, 0, width, height);
        GLFW.glfwSetFramebufferSizeCallback(windowId, (long window, int w, int h) -> {
            width = w;
            height = h;
            GL44.glViewport(0, 0, width, height);
            camera.updateAspectRatio(width, height);
        });
        camera.updateAspectRatio(width, height);

        keyboardCallback = new KeyboardCallback();
		GLFW.glfwSetKeyCallback(windowId, keyboardCallback);
		mouseButtonCallback = new MouseButtonCallback();
		GLFW.glfwSetMouseButtonCallback(windowId, mouseButtonCallback);
		mouseCursorCallback = new MouseCursorCallback();
		GLFW.glfwSetCursorPosCallback(windowId, mouseCursorCallback);
    }
    
    /**
     * Get the current width of the window in pixels
     * @return Pixel width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the current height of the window in pixels
     * @return Pixel height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the current title of the window
     * @return current window title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the window
     * @param title new window title
     */
    public void setTitle(String title) {
        this.title = title;
        GLFW.glfwSetWindowTitle(windowId, title);
    }
    
    /**
	 * Set window visibility
	 * @param show window visibility
	 */
	public void showWindow(boolean show) {
		if(show) {
			GLFW.glfwShowWindow(windowId);
		} else {
			GLFW.glfwHideWindow(windowId);
		}
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


    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowId);
    }

    public void swapBuffers() {
		GLFW.glfwSwapBuffers(windowId);
    }
}
