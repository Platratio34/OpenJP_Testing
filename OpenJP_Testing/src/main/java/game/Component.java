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

    /**
     * Called whenever the a collider on the associated game object is collided with
     * @param other Object this game object is colliding with
     */
    public void onCollision(GameObject other) {

    }

    /**
     * Called when this game object starts colliding with a game object
     * @param other
     */
    public void onCollisionEnter(GameObject other) {

    }

    /**
     * Called when this game object is no longer colliding with a game object
     * @param other
     */
    public void onCollisionExit(GameObject other) {

    }
}
