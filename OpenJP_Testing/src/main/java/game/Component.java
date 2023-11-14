package game;

/**
 * Game object component base class
 */
public abstract class Component {

    /** The game object this component is attached to */
    public GameObject gameObject;
    
    /**
     * Called before the game loop starts, OR when the object is added added to the game.
     * Witch ever happens last.
     */
    public void onStart() {

    }
    
    /**
     * Called once every tick before rendering
     */
    public void onTick() {

    }
    
    /**
     * Called when the associated game object is destroyed
     */
    public void onCleanup() {
        
    }
}
