package input;

import java.util.HashMap;

import org.joml.Vector2f;

/**
 * Keyboard bind input system
 */
public class InputSystem implements KeyboardEvent, MouseEvent {

    private HashMap<String, InputBind> binds = new HashMap<String, InputBind>();

    private Vector2f mousePosition = new Vector2f();

    /**
     * Create an input system
     */
    public InputSystem() { }

    /**
     * Add a named keybind to input system
     * @param name keybind name
     * @param keycode key code (GLFW_KEY_*)
     */
    public void addBind(String name, int keycode) {
        if (binds.containsKey(name)) {
            System.err.println("Bind " + name + " already existed for input system");
            return;
        }
        InputBind bind = new KeyboardBind(keycode);
        bind.name = name;
        binds.put(name, bind);
    }
    
    /**
     * Add a named keybind to input system
     * @param name keybind name
     * @param keycode key code (GLFW_KEY_*)
     * @param cb on change callback
     */
    public void addBind(String name, int keycode, InputCallback cb) {
        if (binds.containsKey(name)) {
            System.err.println("Bind "+name+" already existed for input system");
            return;
        }
        InputBind bind = new KeyboardBind(keycode);
        bind.name = name;
        bind.addCallback(cb);
        binds.put(name, bind);
    }

    /**
     * Add a named mouse keybind to input system
     * @param name keybind name
     * @param keycode key code (GLFW_MOUSE_*)
     */
    public void addMouseBind(String name, int keycode) {
        if (binds.containsKey(name)) {
            System.err.println("Bind "+name+" already existed for input system");
            return;
        }
        MouseBind bind = new MouseBind(keycode);
        bind.name = name;
        binds.put(name, bind);
    }

    /**
     * Add a named axis to input system
     * @param name axis name
     * @param positive positive key code (GLFW_KEY_*)
     * @param negative negative key code (GLFW_KEY_*)
     */
    public void addAxis(String name, int positive, int negative) {
        if (binds.containsKey(name)) {
            System.err.println("Bind "+name+" already existed for input system");
            return;
        }
        InputAxis bind = new InputAxis(positive, negative);
        bind.name = name;
        binds.put(name, bind);
    }

    @Override
    public void onKeyboardEvent(int key, int scanCode, int action, int mods) {
        for (InputBind bind : binds.values()) {
            if (!(bind instanceof MouseBind))
                bind.process(key, action);
        }
    }

    @Override
    public void onMouseButtonEvent(int button, int action, int mods) {
        for (InputBind bind : binds.values()) {
            if (bind instanceof MouseBind)
                bind.process(button, action);
        }
    }

    @Override
    public void onMouseCursorEvent(double xPos, double yPos) {
        mousePosition.x = (float)xPos;
        mousePosition.y = (float)yPos;
    }

    /**
     * Reset keybinds.<br>
     * <br>
     * <b>Only call once at the end of each tick</b>
     */
    public void onTick() {
        for (InputBind bind : binds.values()) {
            bind.reset();
        }
    }

    /**
     * Check if named keybind was just pressed
     * @param bind keybind name
     * @return if the keybind was just pressed
     */
    public boolean pressed(String bind) {
        if (!binds.containsKey(bind) || !(binds.get(bind) instanceof KeyboardBind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return ((KeyboardBind)binds.get(bind)).pressed;
    }

    /**
     * Check if a named keybind is currently down
     * @param bind keybind name
     * @return if the keybind is currently down
     */
    public boolean down(String bind) {
        if (!binds.containsKey(bind) || !(binds.get(bind) instanceof KeyboardBind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return ((KeyboardBind)binds.get(bind)).down;
    }

    /**
     * Check if named keybind was just released
     * @param bind keybind name
     * @return if the keybind was just released
     */
    public boolean released(String bind) {
        if (!binds.containsKey(bind) || !(binds.get(bind) instanceof KeyboardBind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return ((KeyboardBind)binds.get(bind)).released;
    }
    
    /**
     * Get the current value of named axis
     * @param axis axis name
     * @return axis value [-1.0,1.0]
     */
    public float axis(String axis) {
        if (!binds.containsKey(axis) || !(binds.get(axis) instanceof InputAxis)) {
            System.err.println("No axis with name \"" + axis + "\"");
            return 0.0f;
        }
        return ((InputAxis) binds.get(axis)).getAxis();
    }

    /**
     * Get the current mouse position in pixels
     * @return mouse pixel position
     */
    public Vector2f getMousePos() {
        return new Vector2f(mousePosition);
    }

    /**
     * Add a callback to a named bind
     * @param name bind name
     * @param cb on change callback
     */
    public void addBindCallback(String name, InputCallback cb) {
        if (!binds.containsKey(name)) {
            System.err.println("No bind with name \"" + name + "\"");
            return;
        }
        binds.get(name).addCallback(cb);
    }
    
}
