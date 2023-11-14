package input;

import java.util.ArrayList;

/**
 * Generic input binding
 */
public abstract class InputBind {
    
    /** Binding name */
    public String name;

    private ArrayList<InputCallback> callbacks = new ArrayList<InputCallback>();

    /**
     * Process input
     * @param key keyboard or mouse input
     * @param action input action
     */
    public abstract void process(int key, int action);

    /**
     * Reset on tick
     */
    public abstract void reset();

    /**
     * Add an on change callback
     * @param cb on change callback
     */
    public void addCallback(InputCallback cb) {
        callbacks.add(cb);
    }
    /**
     * Remove an on change callback
     * @param cb on change callback
     */
    public void removeCallback(InputCallback cb) {
        callbacks.remove(cb);
    }

    protected void onChange() {
        for (InputCallback inputCallback : callbacks) {
            inputCallback.onChange(this);
        }
    }
    
}
