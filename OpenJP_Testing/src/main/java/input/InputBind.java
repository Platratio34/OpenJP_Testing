package input;

import org.lwjgl.glfw.GLFW;

public class InputBind {
    
    public int keycode;
    public boolean pressed;
    public boolean released;
    public boolean down;

    public InputBind(int keycode) {
        this.keycode = keycode;
    }

    public void reset() {
        pressed = false;
        released = false;
    }

    public void press() {
        pressed = true;
        down = true;
    }

    public void release() {
        released = true;
        down = false;
    }

    public void process(int key, int action) {
        if (key != keycode)
            return;
        if (action == GLFW.GLFW_PRESS)
            press();
        else if (action == GLFW.GLFW_RELEASE)
            release();
    }
}
