package paul.coeus;

import paul.coeus.base.IGameLogic;
import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;
import paul.coeus.objects.Base.Lights.PointLight;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.Base.ShaderHandler.BaseShaderHandler;
import paul.coeus.objects.Base.ShaderHandler.IShaderHandler;
import paul.coeus.objects.Base.ShaderHandler.LightShaderHandler;
import paul.coeus.objects.Base.ShaderHandler.SkyBoxShaderHandler;
import paul.coeus.objects.Base.SkyBox;
import paul.coeus.objects.ImagePlane;
import paul.coeus.utils.IO.MouseInput;
import paul.coeus.utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Engine extends Thread {
    private Window window;
    private IGameLogic gameLogic;
    private Timer timer;
    private Renderer renderer;
    public static final int TARGET_FPS = 60;
    private MouseInput mouseInput;
    public static final int TARGET_UPS = 30;
    Camera camera;

    private HashMap<Class, List<GameObject>> gameObjects;
    private List<GameObject> pureGameObject;
    private List<IShaderHandler> shaders;
    private List<PointLight> pointLightList;
    private PointLight[] pointLights;
    private SkyBox skyBox;


    public Engine(String title, int width, int height, boolean vSync, IGameLogic gameLogic)
    {
        gameObjects = new HashMap<>();
        pureGameObject = new ArrayList<>();
        shaders = new ArrayList<>();
        shaders.add(new BaseShaderHandler());
        pointLightList = new ArrayList<>();
        window = new Window(title, width, height, vSync);
        this.gameLogic = gameLogic;
        mouseInput = new MouseInput();
        timer = new Timer();
        renderer = new Renderer();
        GlobalModules.setRenderer(renderer);
        camera = new Camera();
        GlobalModules.setCamera(camera);
        mouseInput = new MouseInput();

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
            cleanUp();
        }

    }


    private void cleanUp(){

        window.destroyUIStuff();
        renderer.cleanup();
        cleanUpShaders();

        glfwDestroyWindow(window.getWindowHandle());
        glfwTerminate();
    }

    private void cleanUpShaders(){
        for (IShaderHandler sh:
             shaders) {
            sh.getShaderProgram().cleanup();
        }
    }

    public void setSkyBox(String texture)
    {
        removeGameObject(skyBox);
        skyBox =  new SkyBox(texture);
        addGameObject(skyBox);
        addShader(new SkyBoxShaderHandler());
    }

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public void setMouseInput(MouseInput mouseInput) {
        this.mouseInput = mouseInput;
    }

    public GameObject getSelected()
    {
        if(mouseInput.isLeftButtonPressed()) {
            System.out.println(pureGameObject.size());
            return mouseInput.getObjectSelection().selectGameItem( pureGameObject.toArray(new GameObject[pureGameObject.size()]), window, mouseInput.getCurrentPos(), camera);
        }
        return null;
    }

    protected void init() throws Exception {
        timer.init();

        window.init();
        renderer.init(window, shaders);
        skyBox =  new SkyBox("src/main/Texture/skybox.png");
        addGameObject(skyBox);
        addShader(new SkyBoxShaderHandler());
        addShader(new LightShaderHandler());
        mouseInput.init(window);
        gameLogic.init(window,this);




        gameLogic.lateInit();


    }

    public void clearGameObject()
    {
        window.getFrame().getContainer().clearChildComponents();
        gameObjects.clear();
        pureGameObject.clear();
        pointLightList.clear();
        pointLights = new PointLight[0];
    }

    public boolean addGameObject(GameObject gameObject){
        if(!gameObjects.containsKey(gameObject.getShader().getClass()))
        {
            List nList = new ArrayList();
            nList.add(gameObject);
            gameObjects.put(gameObject.getShader().getClass(), nList);
            pureGameObject.add(gameObject);
            return  true;
        }
        else if(!gameObjects.get(gameObject.getShader().getClass()).contains(gameObject))
        {
            gameObjects.get(gameObject.getShader().getClass()).add(gameObject);
            pureGameObject.add(gameObject);
            return true;
        }

        return false;
    }

    public boolean addPointLight(PointLight pointLight)
    {
        if(!pointLightList.contains(pointLight))
        {
            pointLightList.add(pointLight);
            pointLights = pointLightList.toArray(new PointLight[pointLightList.size()]);
            addGameObject(pointLight);
            return  true;
        }
        return false;
    }

    public void addShader(IShaderHandler shaderHandler)
    {
        shaders.add(shaderHandler);
        try {
            renderer.setupShaders(shaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeShader(IShaderHandler shaderHandler)
    {
        shaders.remove(shaderHandler);
    }

    public boolean removePointLight(PointLight pointLight)
    {
        if(pointLightList.contains(pointLight))
        {
            pointLightList.remove(pointLight);
            pointLights = pointLightList.toArray(new PointLight[pointLightList.size()]);
            removeGameObject(pointLight);
            return  true;
        }
        return false;
    }

    public boolean removeGameObject(GameObject gameObject){
        if(gameObjects.containsKey(gameObject.getShader().getClass()))
        {

            pureGameObject.remove(gameObject);
            return gameObjects.get(gameObject.getShader().getClass()).remove(gameObject);
        }
        return false;
    }

    public void setSkyBoxVisibility(boolean t){
        skyBox.setVisible(t);
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

        renderer.render(window,gameObjects,shaders,pointLights,camera);
        window.renderUI();
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
        gameObjects.forEach((k,v) ->{
            for (GameObject g: v) {
                if(g instanceof ImagePlane)
                    ((ImagePlane)g).updateAnimation(interval);
                g.update();
            }
        });
        gameLogic.update(interval);
    }
}
