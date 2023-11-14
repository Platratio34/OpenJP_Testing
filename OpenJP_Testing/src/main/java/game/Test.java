package game;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import gizmos.OriginGizmo;
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

        game.run();
    }

    private static class GameCloser extends Component {
        public void onTick() {
            if(gameObject.inputSystem.pressed("close"))
                gameObject.game.markEnd();
        }
    }
}