package projectDemo;

import org.joml.Vector3f;
import paul.coeus.Engine;
import paul.coeus.base.IGameLogic;
import paul.coeus.base.IScene;
import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class PlaceHolderLogic implements IGameLogic {

    Camera camera;
    Engine engine;
    private  float MOUSE_SENSITIVITY = 0.2f;
    private  float moveSpeed = 0.1f;
    private final Vector3f cameraInc;
    private List<IScene> scenes;

    public Camera getCamera() {
        return camera;
    }

    public Engine getEngine() {
        return engine;
    }

    public  float getMouseSensitivity() {
        return MOUSE_SENSITIVITY;
    }

    public  float getMoveSpeed() {
        return moveSpeed;
    }

    public PlaceHolderLogic(){
        cameraInc = new Vector3f();
    }
    @Override
    public void init(Window window, Engine engine) throws Exception {
        camera = new Camera();
        this.engine = engine;
        engine.setCamera(camera);
        scenes = new ArrayList<>();
    }

    @Override
    public void input(Window window) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }

        camera.movePosition(cameraInc.x*moveSpeed, cameraInc.y*moveSpeed,cameraInc.z*moveSpeed);
    }

    @Override
    public void update(float interval) {

    }

    @Override
    public void lateInit() {
        startNewScene(new DemoScene());
    }

    @Override
    public void startNewScene(IScene scene) {
        try {
            engine.clearGameObject();
            scene.init(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
