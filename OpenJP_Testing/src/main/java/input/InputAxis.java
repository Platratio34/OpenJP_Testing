package input;

import org.lwjgl.glfw.GLFW;

/**
 * Keyboard input axis
 */
public class InputAxis extends InputBind {

    /** Axis positive keyboard keycode (<code>GLFW.GLFW_KEY_*</code>) */
    public int positive;
    private float pos;
    /** Axis negative keyboard keycode (<code>GLFW.GLFW_KEY_*</code>) */
    public int negative;
    private float neg;

    /**
     * Create a new keyboard input axis
     * @param p GLFW positive keycode (<code>GLFW.GLFW_KEY_*</code>)
     * @param n GLFW negative keycode (<code>GLFW.GLFW_KEY_*</code>)
     */
    public InputAxis(int p, int n) {
        positive = p;
        negative = n;
    }

    /**
     * Process keyboard input
     * @param key GLFW keycode
     * @param action GLFW key action
     */
    public void process(int key, int action) {
        if (key == positive) {
            if (action == GLFW.GLFW_PRESS) {
                pos = 1.0f;
                onChange();
            } else if (action == GLFW.GLFW_RELEASE) {
                pos = 0.0f;
                onChange();
            }
        } else if (key == negative) {
            if (action == GLFW.GLFW_PRESS) {
                neg = 1.0f;
                onChange();
            } else if (action == GLFW.GLFW_RELEASE) {
                neg = 0.0f;
                onChange();
            }
        }
    }

    /**
     * Get the axis value
     * @return axis value (<code>-1.0f</code>-<code>1.0f</code>)
     */
    public float getAxis() {
        return pos - neg;
    }

    @Override
    public void reset() {}
}
