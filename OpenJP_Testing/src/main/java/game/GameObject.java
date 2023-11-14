package game;

import java.util.ArrayList;

import gizmos.Gizmo;
import input.InputSystem;
import objects.Renderer;
import objects.Transform;

public class GameObject {
    
    public Transform transform;

    private ArrayList<Component> components;
    private boolean started = false;

    protected InputSystem inputSystem;
    protected Game game;

    public Renderer renderer;
    public Gizmo gizmo;

    public GameObject() {
        transform = new Transform();
        components = new ArrayList<Component>();
    }

    public void addComponent(Component component) {
        components.add(component);
        component.gameObject = this;
        if (started) {
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
                System.err.println("[WARN] game object already had a renderer, previous renderer will no longer be drawn");
            }
            renderer = (Renderer) component;
            renderer.transform = transform;
        }
    }

    public void removeComponent(Component component) {
        if (renderer == component)
            renderer = null;
        if (gizmo == component)
            gizmo = null;
        components.remove(component);
    }

    public void onStart() {
        started = true;
        for (Component component : components) {
            component.onStart();
        }
    }
    
    public void onTick() {
        for (Component component : components) {
            component.onTick();
        }
    }

    public void cleanup() {
        for (Component component : components) {
            component.onCleanup();
        }
    }
}
