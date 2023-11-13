package input;

/**
 * Mouse button and movement input event
 */
public interface MouseEvent {

    /**
     * Called on mouse button input
     * @param button GLFW mouse button code (<code>GLFW.GLFW_MOUSE_*</code>)
     * @param action GLFW action (<code>GLFW.GLFW_PRESS</code>, <code>GLFW.GLFW_RELEASE</code>, <code>GLFW.GLFW_REPEAT</code>)
     * @param mods bitfield describing which modifiers keys were held down
     */
    public void onMouseButtonEvent(int button, int action, int mods);

    /**
     * Called on mouse movement
     * @param xPos top left horizontal position of the mouse
     * @param yPos top left vertical position of the mouse
     */
    public void onMouseCursorEvent(double xPos, double yPos);
}
