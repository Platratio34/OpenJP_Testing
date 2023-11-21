package game;

import java.util.ArrayList;

import collision.Collider;
import gizmos.Gizmo;
import input.InputSystem;
import objects.Renderer;
import objects.Transform;

/**
 * Object within the game world
 */
public class GameObject {
    
    /** Transformation and parenting of this object */
    public Transform transform;

    private ArrayList<Component> components;
    private boolean started = false;
    
    protected InputSystem inputSystem;
    protected Game game;

    /** Primary renderer of this object */
    public Renderer renderer;
    /** Primary gizmo of this object */
    public Gizmo gizmo;

    private ArrayList<Collider> colliders;
    private ArrayList<GameObject> newCollided;
    private ArrayList<GameObject> collidedWith;

    /** Name of this object, used for searching with <code>Game.find(String)</code> and debug */
    public String name;

    /**
     * Create a new game object
     */
    public GameObject() {
        transform = new Transform();
        components = new ArrayList<Component>();
        colliders = new ArrayList<Collider>();
        newCollided = new ArrayList<GameObject>();
        collidedWith = new ArrayList<GameObject>();
    }

    /**
     * Add a component to this game object.<br>
     * <br>
     * Will call <code>Component.onStart()</code> if added after the game object has been started.
     * <h4>Renderers and Gizmos</h4>
     * If the component being added is a renderer or gizmo, it will overwrite the current primary renderer or gizmo respectively.
     * @param component component to add
     */
    public void addComponent(Component component) {
        components.add(component);
        component.gameObject = this;
        if (started) {
            component.game = game;
            component.onStart();
        }
        if (component instanceof Gizmo) {
            if (gizmo != null) {
                System.err.println("[WARN] game object already had a gizmo, previous gizmo will no longer be drawn");
            }
            gizmo = (Gizmo) component;
            gizmo.setParent(transform);
        } else if (component instanceof Renderer) {
            if (renderer != null) {
                System.err.println(
                        "[WARN] game object already had a renderer, previous renderer will no longer be drawn");
            }
            renderer = (Renderer) component;
            renderer.transform = transform;
        } else if (component instanceof Collider) {
            Collider collider = (Collider) component;
            colliders.add(collider);
        }
    }

    /**
     * Remove a component from the game object.
     * <h4>Renderers and Gizmos</h4>
     * If the component being removed is a renderer or gizmo, and it is currently the primary one, it will be unset on this game object
     * @param component component to remove
     */
    public void removeComponent(Component component) {
        if (renderer == component)
            renderer = null;
        else if (gizmo == component)
            gizmo = null;
        else if (component instanceof Collider) {
            Collider collider = (Collider) component;
            colliders.remove(collider);
        }
        components.remove(component);
    }

    /**
     * Called by the game controller when it starts, or the object is added, whichever happens later
     */
    public void onStart() {
        started = true;
        for (Component component : components) {
            component.game = game;
            component.onStart();
        }
    }
    
    /**
     * Called every tick before collisions and render
     */
    public void onTick() {
        for (Component component : components) {
            component.onTick();
        }
    }

    /**
     * Called when the object is destroyed or the game closes
     */
    public void cleanup() {
        for (Component component : components) {
            component.onCleanup();
        }
    }

    /**
     * Check for collisions with another game object, and <b>will</b> trigger collision events
     * @param other object to check for collisions with
     */
    public void checkCollision(GameObject other) {
        for (Collider collider : colliders) {
            if (other.checkCollision(collider)) {
                onCollision(other);
                other.onCollision(this);
                return;
            }
        }
    }
    
    /**
     * Check for collisions with another game object, does <b>not</b> trigger collision events
     * @param other object to check for collisions with
     * @return If this game object is colliding with the other
     */
    public boolean tryCollision(GameObject other) {
        for (Collider collider : colliders) {
            if (other.checkCollision(collider)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check for collisions with another game object, does <b>not</b> trigger collision events
     * @param other object to check for collisions with
     * @param mask collision mask to check with
     * @return If this game object is colliding with the other
     * @see Collider#mask
     */
    public boolean tryCollision(GameObject other, byte mask) {
        for (Collider collider : colliders) {
            if (other.checkCollision(collider, mask)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if this game object is no longer colliding with any other objects and trigger <code>Component.onCollisionExit(GameObject)</code>
     */
    public void checkCollisionExit() {
        for (GameObject gameObject : collidedWith) {
            if (!newCollided.contains(gameObject)) {
                for (Component component : components) {
                    component.onCollisionExit(gameObject);
                }
            }
        }
        collidedWith.clear();
        collidedWith = newCollided;
        newCollided = new ArrayList<GameObject>();
    }

    protected void onCollision(GameObject other) {
        if (newCollided.contains(other))
            return;

        for (Component component : components) {
            component.onCollision(other);
        }
        newCollided.add(other);
        if (!collidedWith.contains(other)) {
            for (Component component : components) {
                component.onCollisionEnter(other);
            }
        }
    }
    
    /**
     * Check if the given collider is colliding with this game object
     * @param other collider to check with
     * @return If the they are colliding
     */
    public boolean checkCollision(Collider other) {
        for (Collider collider : colliders) {
            if (collider.checkCollision(other)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Check if the given collider is colliding with this game object
     * @param other collider to check with
     * @param mask collision mask to check with
     * @return If the they are colliding
     * @see Collider#mask
     */
    public boolean checkCollision(Collider other, byte mask) {
        for (Collider collider : colliders) {
            if (collider.checkCollision(other, mask)) {
                return true;
            }
        }
        return false;
    }
}
