package game.test;

import java.awt.Color;

import game.Component;
import game.Game;
import game.GameObject;
import objects.MeshCache;
import objects.MeshRenderer;
import shaders.Material;
import util.CubicBezier;

public class TestDoor extends Component {

    private GameObject door1;
    private GameObject door2;

    private boolean open = false;
    private float state = 0;

    private CubicBezier curve;

    protected TestDoor(GameObject d1, GameObject d2) {
        door1 = d1;
        door2 = d2;
        curve = new CubicBezier();
    }

    @Override
    public void onTick() {
        if (Game.inputSystem.pressed("door")) {
            open = !open;
        }

        if (open) {
            if (state < 1) {
                state += Game.deltaTime()/2f;
            }
            if (state > 1) {
                state = 1;
            }
        } else {
            if (state > 0) {
                state -= Game.deltaTime()/2f;
            }
            if (state < 0) {
                state = 0;
            }
        }

        float x = curve.evaluate(state);
        door1.transform.setPosition(-x, 0.5f, 0);
        door2.transform.setPosition(x, 0.5f, 0);
    }


    public static TestDoor create() {
        GameObject parent = new GameObject();
        GameObject door1 = createDoor();
        GameObject door2 = createDoor();
        door1.transform.setParent(parent.transform);
        door2.transform.setParent(parent.transform);
        // door2.transform.setScale(-1,1,1);
        door2.transform.setRotation(0, 180, 0);

        TestDoor door = new TestDoor(door1, door2);
        parent.addComponent(door);

        Game.addGameObject(parent);
        Game.addGameObject(door1);
        Game.addGameObject(door2);

        return door;
    }

    private static GameObject createDoor() {
        GameObject door = new GameObject();
        MeshRenderer doorRenderer = new MeshRenderer(MeshCache.getMesh("MilShip1_BulkHeadDoor.obj"));
        doorRenderer.materials.setMaterial(0, new Material(new Color(200, 200, 200), 0.25f));
        doorRenderer.materials.setMaterial(1, new Material(new Color(100, 100, 100), 0.75f));
        door.addComponent(doorRenderer);
        door.transform.setPosition(0, 0.5f, 0);
        
        return door;
    }
}
