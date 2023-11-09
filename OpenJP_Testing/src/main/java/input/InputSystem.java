package input;

import java.util.HashMap;

public class InputSystem implements KeyboardEvent {

    private HashMap<String, InputBind> binds;
    private HashMap<String, InputAxis> axies;

    public InputSystem() {
        binds = new HashMap<String, InputBind>();
        axies = new HashMap<String, InputAxis>();
    }

    /**
     * Add a named keybind to input system
     * @param name keybind name
     * @param keycode key code (GLFW_KEY_*)
     */
    public void addBind(String name, int keycode) {
        InputBind bind = new InputBind(keycode);
        binds.put(name, bind);
    }

    /**
     * Add a named axis to input system
     * @param name axis name
     * @param positive positive key code (GLFW_KEY_*)
     * @param negative negative key code (GLFW_KEY_*)
     */
    public void addAxis(String name, int positive, int negative) {
        axies.put(name, new InputAxis(positive, negative));
    }

    @Override
    public void onKeyboardEvent(int key, int scancode, int action, int mods) {
        for (InputBind bind : binds.values()) {
            bind.process(key, action);
        }
        for (InputAxis axis : axies.values()) {
            axis.process(key, action);
        }
    }

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
        if (!binds.containsKey(bind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return binds.get(bind).pressed;
    }

    /**
     * Check if a named keybind is currently down
     * @param bind keybind name
     * @return if the keybind is currently down
     */
    public boolean down(String bind) {
        if (!binds.containsKey(bind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return binds.get(bind).down;
    }

    /**
     * Check if named keybind was just released
     * @param bind keybind name
     * @return if the keybind was just released
     */
    public boolean released(String bind) {
        if (!binds.containsKey(bind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return binds.get(bind).released;
    }
    
    /**
     * Get the current value of named axis
     * @param axis axis name
     * @return axis value [-1.0,1.0]
     */
    public float axis(String axis) {
        if (!axies.containsKey(axis)){
            System.err.println("No axis with name \""+axis+"\"");
            return 0.0f;
        }
        return axies.get(axis).getAxis();
    }
    
}
