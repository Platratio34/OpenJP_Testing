package game;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import game.simple.FirstPersonFlyCamera;
import gizmos.OriginGizmo;
import input.InputBind;
import input.InputCallback;
import input.KeyboardBind;
import objects.MeshCache;
import objects.MeshRenderer;
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
        gOClose.addComponent(mR);

        game.mainCamera.transform.setPosition(3.0f, 5.0f, 3.0f);
        game.mainCamera.transform.setRotation(35, -45, 0);

        gOClose.addComponent(new OriginGizmo());

        game.drawGizmos = true;

        game.addGameObject(FirstPersonFlyCamera.create());

        game.lightingSettings.setAmbientLighting(Color.WHITE);

        game.inputSystem.addBind("toggleGizmos", GLFW.GLFW_KEY_1, new InputCallback() {

            @Override
            public void onChange(InputBind bind) {
                KeyboardBind b = (KeyboardBind) bind;
                if(b.pressed)
                    game.drawGizmos = !game.drawGizmos;
            }
            
        });

        game.run();
    }

    private static class GameCloser extends Component {
        public void onTick() {
            if(gameObject.inputSystem.pressed("close"))
                gameObject.game.markEnd();
        }
    }
}