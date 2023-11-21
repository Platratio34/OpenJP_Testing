package game.simple;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;

import game.Game;
import game.Component;
import game.GameObject;
import gizmos.Gizmo;
import gizmos.GizmoType;
import objects.Camera;

public class FirstPersonFlyCamera extends Component {
    
    private Camera camera;
    private Gizmo gizmo;

    public static final String AXIS_FB = "forward/backward";
    public static final String AXIS_LR = "left/right";
    public static final String AXIS_UD = "up/down";
    public static final String BIND_MOVE_MOD = "moveMod";

    public static final String BIND_VIEW = "view";

    /** Lateral move speed */
    public float moveSpeed = 5f;
    public float moveMod = 2f;

    private Vector2f lastPos;

    @Override
    public void onStart() {
        camera = Game.mainCamera;
        camera.setParent(gameObject.transform);
        if (gizmo != null) {
            System.out.println("Thing-ing");
            gizmo.transform.setParent(camera.transform);
            float distFromCam = 0.5f;
            float height = distFromCam * (float)Math.tan(camera.fov/2) + 0.01f;
            float width = height * camera.aspectRatio;
            gizmo.transform.setScale(width, height, distFromCam * 2f);
            gizmo.setVisible(false);
        }

        Game.inputSystem.addAxis(AXIS_FB, GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_S);
        Game.inputSystem.addAxis(AXIS_LR, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D);
        Game.inputSystem.addAxis(AXIS_UD, GLFW.GLFW_KEY_Q, GLFW.GLFW_KEY_Z);
        Game.inputSystem.addBind(BIND_MOVE_MOD, GLFW.GLFW_KEY_LEFT_SHIFT);

        Game.inputSystem.addMouseBind(BIND_VIEW, GLFW.GLFW_MOUSE_BUTTON_2);
    }

    @Override
    public void onTick() {
        Vector3f move = new Vector3f();
        move.add(gameObject.transform.forward().mul(Game.inputSystem.axis(AXIS_FB)));
        move.add(gameObject.transform.right().mul(Game.inputSystem.axis(AXIS_LR) * -1f));
        move.add(gameObject.transform.up().mul(Game.inputSystem.axis(AXIS_UD)));

        if (move.length() > 0) {
            move.mul(moveSpeed * Game.deltaTime());
            if (Game.inputSystem.down(BIND_MOVE_MOD)) {
                move.mul(moveMod);
            }
            gameObject.transform.translate(move);
            // gizmo.transform.setPosition(gameObject.transform.getPosition());
        }

        Vector2f mousePos = Game.inputSystem.getMousePos();
        if (Game.inputSystem.pressed(BIND_VIEW)) {
            lastPos = mousePos;
        } else if (Game.inputSystem.down(BIND_VIEW)) {
            Vector2f diff = lastPos.sub(mousePos);
            Vector3f rot = camera.transform.getRotation();
            rot.add(-diff.y, 0.0f, 0.0f);
            if (rot.x > 85) { rot.x = 85; }
            if (rot.x < -85) { rot.x = -85; }
            camera.transform.setRotation(rot.x, 0.0f, 0.0f);
            gameObject.transform.rotate(0.0f, -diff.x, 0.0f);
            // rot.y = gameObject.transform.getRotation().y;
            // gizmo.transform.setRotation(-rot.x*2f, -rot.y*2f, 0.0f);
        }
        lastPos = mousePos;
    }

    @Override
    public void onCleanup() {
        camera.setParent(null);
    }

    public void setGizmo(Gizmo gizmo) {
        this.gizmo = gizmo;
    }
    
    public static GameObject create() {
        GameObject gO = new GameObject();
        FirstPersonFlyCamera fPCam = new FirstPersonFlyCamera();
        gO.addComponent(fPCam);
        Gizmo gizmo = new Gizmo(GizmoType.CAMERA);
        gO.addComponent(gizmo);
        gizmo.setColor(Color.WHITE);
        // gizmo.setSize(2f);
        fPCam.setGizmo(gizmo);
        return gO;
    }
}
