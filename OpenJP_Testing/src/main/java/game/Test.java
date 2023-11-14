package game;

import java.awt.Color;
import java.io.IOException;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import game.simple.FirstPersonFlyCamera;
import game.simple.PrimitiveCreator;
import gizmos.OriginGizmo;
import input.InputBind;
import input.InputCallback;
import input.KeyboardBind;
import objects.MeshCache;
import objects.MeshRenderer;
import shaders.Material;
import shaders.Shaders;

public class Test {

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.inputSystem.addBind("close", GLFW.GLFW_KEY_ESCAPE);
        GameObject gOClose = new GameObject();
        GameCloser gC = new GameCloser();
        gOClose.addComponent(gC);
        game.addGameObject(gOClose);

        MeshCache.load("meshes/matCube.bin");
        MeshRenderer mR = new MeshRenderer(MeshCache.getMesh("meshes/matCube.bin"));
        mR.setShader(Shaders.getShader(Game.MAIN_SHADER));
        mR.materials.setMaterial(0, new Material(Color.white));
        gOClose.addComponent(mR);

        game.mainCamera.transform.setPosition(3.0f, 5.0f, 3.0f);
        game.mainCamera.transform.setRotation(35, -45, 0);

        gOClose.addComponent(new OriginGizmo());

        game.drawGizmos = true;

        game.addGameObject(FirstPersonFlyCamera.create());

        game.lightingSettings.setAmbientLighting(new Color(0.1f, 0.1f, 0.1f));
        game.lightingSettings.setGlobalLightColor(new Color(0.95f, 0.90f, 0.85f));
        game.lightingSettings.setGlobalLightDirection(new Vector3f(0.5f, 0.3f, 0.1f));

        game.inputSystem.addBind("toggleGizmos", GLFW.GLFW_KEY_1, new InputCallback() {

            @Override
            public void onChange(InputBind bind) {
                KeyboardBind b = (KeyboardBind) bind;
                if(b.pressed)
                    game.drawGizmos = !game.drawGizmos;
            }
            
        });

        GameObject plane = PrimitiveCreator.createPlane();
        plane.transform.setScale(100, 100, 100);
        plane.transform.setPosition(0, -0.5f, 0);
        plane.renderer.materials.getMaterial(0).setColor(new Color(82, 69, 11));
        game.addGameObject(plane);

        game.run();
    }

    private static class GameCloser extends Component {
        public void onTick() {
            if(gameObject.inputSystem.pressed("close"))
                gameObject.game.markEnd();
        }
    }
}