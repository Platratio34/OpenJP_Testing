package input;

/**
 * GLFW on keyboard input event
 */
public interface KeyboardEvent {
    /**
     * Called on keyboard input
     * @param key GLFW keycode (<code>GLFW.GLFW_KEY_*</code>)
     * @param scanCode Platform specific code
     * @param action GLFW action (<code>GLFW.GLFW_PRESS</code>, <code>GLFW.GLFW_RELEASE</code>, <code>GLFW.GLFW_REPEAT</code>)
     * @param mods bitfield describing which modifiers keys were held down
     */
    public void onKeyboardEvent(int key, int scanCode, int action, int mods);
}
