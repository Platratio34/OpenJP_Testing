package input;

import org.lwjgl.glfw.GLFW;

public class InputAxis {
    
    // public float value;

    public int positive;
    private boolean pos;
    public int negative;
    private boolean neg;

    public InputAxis(int p, int n) {
        positive = p;
        negative = n;
    }

    public void process(int key, int action) {
        if (key == positive) {
            if (action == GLFW.GLFW_PRESS)
                pos = true;
            else if (action == GLFW.GLFW_RELEASE)
                pos = false;
        } else if (key == negative) {
            if (action == GLFW.GLFW_PRESS)
                neg = true;
            else if (action == GLFW.GLFW_RELEASE)
                neg = false;
        }
    }

    public float getAxis() {
        return ( pos ? 1.0f : 0.0f ) + ( neg ? -1.0f : 0.0f);
    }
}
