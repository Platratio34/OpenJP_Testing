package input;

import org.lwjgl.glfw.GLFW;

public class InputAxis {
    
    // public float value;

    public int positive;
    private float pos;
    public int negative;
    private float neg;

    public InputAxis(int p, int n) {
        positive = p;
        negative = n;
    }

    public void process(int key, int action) {
        if (key == positive) {
            if (action == GLFW.GLFW_PRESS)
                pos = 1.0f;
            else if (action == GLFW.GLFW_RELEASE)
                pos = 0.0f;
        } else if (key == negative) {
            if (action == GLFW.GLFW_PRESS)
                neg = 1.0f;
            else if (action == GLFW.GLFW_RELEASE)
                neg = 0.0f;
        }
    }

    public float getAxis() {
        return pos - neg;
    }
}
