package game.simple;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import game.Component;
import game.GameObject;
import objects.Camera;

public class FirstPersonFlyCamera extends Component {
    
    private Camera camera;

    public static final String AXIS_FB = "forward/backward";
    public static final String AXIS_LR = "left/right";
    public static final String AXIS_UD = "up/down";

    public static final String BIND_MOVE = "view";

    /** Lateral move speed */
    public float moveSpeed = 5f;

    private Vector2f lastPos;

    @Override
    public void onStart() {
        camera = game.mainCamera;
        // camera.setParent(gameObject.transform);

        game.inputSystem.addAxis(AXIS_FB, GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_S);
        game.inputSystem.addAxis(AXIS_LR, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D);
        game.inputSystem.addAxis(AXIS_UD, GLFW.GLFW_KEY_Q, GLFW.GLFW_KEY_Z);

        game.inputSystem.addMouseBind(BIND_MOVE, GLFW.GLFW_MOUSE_BUTTON_2);
    }

    @Override
    public void onTick() {
        Vector3f move = new Vector3f();
        move.add(camera.transform.forward().mul(game.inputSystem.axis(AXIS_FB)));
        move.add(camera.transform.right().mul(game.inputSystem.axis(AXIS_LR) * -1f));
        move.add(camera.transform.up().mul(game.inputSystem.axis(AXIS_UD)));

        if (move.length() > 0) {
            move.mul(moveSpeed * game.deltaTime());
            camera.transform.translate(move);
        }

        Vector2f mousePos = game.inputSystem.getMousePos();
        if (game.inputSystem.pressed(BIND_MOVE)) {
            lastPos = mousePos;
        } else if (game.inputSystem.down(BIND_MOVE)) {
            Vector2f diff = lastPos.sub(mousePos);
            Vector3f rot = camera.transform.getRotation();
            rot.add(-diff.y, -diff.x, 0);
            if (rot.x > 85) { rot.x = 85; }
            if (rot.x < -85) { rot.x = -85; }
            camera.transform.setRotation(rot);
        }
        lastPos = mousePos;
    }

    @Override
    public void onCleanup() {
        camera.setParent(null);
    }
    
    public static GameObject create() {
        GameObject gO = new GameObject();
        gO.addComponent(new FirstPersonFlyCamera());
        return gO;
    }
}
