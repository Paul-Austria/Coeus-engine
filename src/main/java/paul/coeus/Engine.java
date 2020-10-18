package paul.coeus;

import org.joml.Vector4f;
import paul.coeus.base.IGameLogic;
import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.Textures.Texture;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.ImagePlane;
import paul.coeus.utils.IO.MouseInput;
import paul.coeus.utils.LoadObjects;
import paul.coeus.utils.Timer;

import java.util.ArrayList;
import java.util.List;

public class Engine extends Thread {
    private Window window;
    private IGameLogic gameLogic;
    private Timer timer;
    private Renderer renderer;
    public static final int TARGET_FPS = 60;
    private MouseInput mouseInput;
    public static final int TARGET_UPS = 30;
    Camera camera;

    private List<GameObject> gameObjectList;
    private GameObject[] gameObjectArray;



    public Engine(String title, int width, int height, boolean vSync, IGameLogic gameLogic)
    {
        gameObjectList = new ArrayList<>();
        window = new Window(title, width, height, vSync);
        this.gameLogic = gameLogic;
        mouseInput = new MouseInput();
        timer = new Timer();
        renderer = new Renderer();
        camera = new Camera();
    }

    @Override
    public void run(){
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
        finally {
            System.out.println("Not Implemented");
        }

    }

    protected void init() throws Exception {
        timer.init();
        gameLogic.init(window,this);
        window.init();
        renderer.init(window);
        gameLogic.lateInit();
    }

    public void clearGameObject()
    {
        gameObjectList.clear();
        gameObjectArray = new GameObject[0];
    }

    public boolean addGameObject(GameObject gameObject){
        if(!gameObjectList.contains(gameObject))
        {
            gameObjectList.add(gameObject);
            gameObjectArray = gameObjectList.toArray(new GameObject[gameObjectList.size()]);
            return  true;
        }
        return false;
    }
    public boolean removeGameObject(GameObject gameObject){
        if(gameObjectList.contains(gameObject))
        {
            gameObjectList.remove(gameObject);
            gameObjectArray = gameObjectList.toArray(new GameObject[gameObjectList.size()]);
            return  true;
        }
        return false;
    }


    protected void gameLoop(){
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;
        System.out.println(interval);
        boolean running = true;

        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;
            while (accumulator >= interval) {
                update(interval);
                accumulator = 0;

            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    private void render(){
        renderer.render(window,gameObjectArray,camera);
        window.update();
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    protected void update(float interval){
        gameLogic.input(window);
        for (GameObject g: gameObjectList) {
            if(g instanceof ImagePlane)
            {
                ((ImagePlane)g).updateAnimation(interval);
            }
        }
        gameLogic.update(interval);
    }
}
