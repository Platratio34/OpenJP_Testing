package input;

import java.util.HashMap;

public class InputSystem implements KeyboardEvent {

    private HashMap<String, InputBind> binds;
    private HashMap<String, InputAxis> axies;

    public InputSystem() {
        binds = new HashMap<String, InputBind>();
        axies = new HashMap<String, InputAxis>();
    }

    public void addBind(String name, int keycode) {
        InputBind bind = new InputBind(keycode);
        binds.put(name, bind);
    }

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

    public boolean pressed(String bind) {
        if (!binds.containsKey(bind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return binds.get(bind).pressed;
    }

    public boolean down(String bind) {
        if (!binds.containsKey(bind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return binds.get(bind).down;
    }

    public boolean released(String bind) {
        if (!binds.containsKey(bind)) {
            System.err.println("No bind with name \""+bind+"\"");
            return false;
        }
        return binds.get(bind).released;
    }
    
    public float axis(String axis) {
        if (!axies.containsKey(axis)){
            System.err.println("No axis with name \""+axis+"\"");
            return 0.0f;
        }
        return axies.get(axis).getAxis();
    }
    
}
