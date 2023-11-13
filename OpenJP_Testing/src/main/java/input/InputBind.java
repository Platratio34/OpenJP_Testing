package input;

import org.lwjgl.glfw.GLFW;

/**
 * Keyboard input binding
 */
public class InputBind {
    
    /**
     * Binding keycode (<code>GLFW.GLFW_KEY_*</code>)
     */
    public int keycode;
    /**
     * If the bind was pressed since last tick
     */
    public boolean pressed;
    /**
     * If the bind was released since last tick
     */
    public boolean released;
    /**
     * If the bind is currently down
     */
    public boolean down;

    /**
     * Create a new keyboard binding
     * @param keycode GLFW keycode (<code>GLFW.GLFW_KEY_*</code>)
     */
    public InputBind(int keycode) {
        this.keycode = keycode;
    }

    /**
     * Reset the bind pressed and released state
     */
    public void reset() {
        pressed = false;
        released = false;
    }

    /**
     * Mark the bind as pressed this tick
     */
    public void press() {
        pressed = true;
        down = true;
    }

    /**
     * Mark the bind as released this tick
     */
    public void release() {
        released = true;
        down = false;
    }

    /**
     * Process keyboard input
     * @param key GLFW keycode
     * @param action GLFW key action
     */
    public void process(int key, int action) {
        if (key != keycode)
            return;
        if (action == GLFW.GLFW_PRESS)
            press();
        else if (action == GLFW.GLFW_RELEASE)
            release();
    }
}
