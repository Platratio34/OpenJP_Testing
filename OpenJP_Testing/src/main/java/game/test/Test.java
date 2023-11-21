package game.test;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import collision.Collider;
import game.Component;
import game.Game;
import game.GameObject;
import game.simple.FirstPersonFlyCamera;
import game.simple.PrimitiveCreator;
import gizmos.OriginGizmo;
import input.InputBind;
import input.InputCallback;
import input.KeyboardBind;
import meshLoaders.ObjLoader;
import objects.Mesh;
import objects.MeshCache;
import objects.MeshRenderer;
import objects.Texture2D;
import shaders.Material;
import shaders.Shaders;

public class Test {

    public static void main(String[] args) throws IOException {
        util.BinMesh.gizmoResourceToBin("meshes/gizmos/camera.gizmo", "src/main/resources/meshes/gizmos/camera.gzb");
        
        Game game = new Game();

        game.inputSystem.addBind("close", GLFW.GLFW_KEY_ESCAPE);
        GameObject gOClose = new GameObject();
        GameCloser gC = new GameCloser();
        gOClose.addComponent(gC);
        game.addGameObject(gOClose);

        MeshCache.load("meshes/matCube.bin");
        MeshRenderer mR = new MeshRenderer(MeshCache.getMesh("meshes/matCube.bin"));
        mR.setShader(Shaders.getShader(Game.MAIN_SHADER));
        mR.materials.setMaterial(0, new Material(new Color(255, 255, 255)));
        mR.defferTransparency = true;
        Collider gOCCollider = new Collider(MeshCache.getMeshData("meshes/matCube.bin"));
        gOClose.addComponent(gOCCollider);
        gOClose.addComponent(mR);

        // game.mainCamera.transform.setPosition(3.0f, 5.0f, 3.0f);
        // game.mainCamera.transform.setRotation(35, -45, 0);

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
        game.inputSystem.addBind("toggleAlpha", GLFW.GLFW_KEY_2, new InputCallback() {
            private boolean al = false;

            @Override
            public void onChange(InputBind bind) {
                KeyboardBind b = (KeyboardBind) bind;
                if (b.pressed) {
                    al = !al;
                    if (al) {
                        mR.materials.getMaterial(0).setColor(new Color(255, 255, 255, 128));
                    } else {
                        mR.materials.getMaterial(0).setColor(new Color(255, 255, 255, 255));
                    }
                }
            }
            
        });

        GameObject plane = PrimitiveCreator.createPlaneCollision();
        plane.transform.setScale(100, 100, 100);
        plane.transform.setPosition(0, -0.5f, 0);
        plane.name = "Plane";
        Material mat = plane.renderer.materials.getMaterial(0);
        mat.setColor(new Color(82, 69, 11));
        mat.setTexture(Texture2D.loadFromPngResource("textures/checkerboard16_2.png"));
        mat.setTextureScale(5, 5);
        game.addGameObject(plane);

        GameObject sphere = new GameObject();
        sphere.transform.setPosition(0, 3, 0);
        // MeshData sphereData = CircleBuilder.buildSphere(1, 20);
        // BinMesh.meshDataToFile(sphereData, "src/main/resources/meshes/sphere.bin");
        MeshRenderer sMR = new MeshRenderer(MeshCache.getMesh("meshes/sphere.bin"));
        sMR.setShader(Shaders.getShader(Game.MAIN_SHADER));
        // sMR.defferRender = true;
        sMR.materials.setMaterial(0, new Material());
        sphere.addComponent(sMR);
        game.addGameObject(sphere);

        GameObject door = new GameObject();
        BufferedReader doorReader = new BufferedReader(new FileReader("MilShip1_BulkHeadDoor.obj"));
        MeshRenderer doorRenderer = new MeshRenderer(new Mesh(ObjLoader.parseFile(doorReader)));
        doorRenderer.materials.setMaterial(0, new Material(new Color(200, 200, 200), 0.25f));
        doorRenderer.materials.setMaterial(1, new Material(new Color(100, 100, 100), 0.75f));
        door.addComponent(doorRenderer);
        // doorRenderer.defferRender = true;
        game.addGameObject(door);

        GameObject colliderTester = new GameObject();
        colliderTester.addComponent(new CollisionTester());
        Collider testCollider = new Collider(MeshCache.getMeshData("meshes/matCube.bin"));
        colliderTester.addComponent(testCollider);
        MeshRenderer testColliderMesh = new MeshRenderer(MeshCache.getMesh("meshes/matCube.bin"));
        testColliderMesh.materials.setMaterial(0, new Material());
        colliderTester.addComponent(testColliderMesh);
        game.addGameObject(colliderTester);
        colliderTester.transform.setPosition(0,10f,0);
        // colliderTester.transform.setScale(0.5f, 0.5f, 0.5f);
        sphere.transform.setParent(colliderTester.transform);

        // testCollider.mask;
        
        game.run();
    }

    private static class GameCloser extends Component {
        public void onTick() {
            if (game.inputSystem.pressed("close"))
                game.markEnd();
        }
    }
    
    private static class CollisionTester extends Component {

        private float dir = -1;
        private Material mat;
        private float speed = 0;
        private boolean doGravity = true;

        @Override
        public void onStart() {
            mat = gameObject.renderer.materials.getMaterial(0);
        }

        @Override
        public void onTick() {
            if (doGravity) {
                speed -= 9.8 * game.deltaTime();
                gameObject.transform.translate(0, speed * game.deltaTime() , 0);
            }
            // Vector3f pos = gameObject.transform.getPosition();
            // if (pos.y > 3)
            //     dir = -1;
            // else if (pos.x < -2)
            //     dir = 1;
        }
        
        @Override
        public void onCollisionEnter(GameObject other) {
            mat.setColor(new Color(255, 0, 0));
            speed = 0;
            doGravity = false;
            // dir = 1;
            // gameObject.transform.translate(0,15,0);
        }
        @Override
        public void onCollisionExit(GameObject other) {
            mat.setColor(new Color(0, 255, 0));
        }
    }
}