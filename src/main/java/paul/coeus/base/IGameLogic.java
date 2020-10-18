package paul.coeus.base;

import paul.coeus.Engine;
import paul.coeus.graphics.Camera;

public interface IGameLogic {
    void init(Window window, Engine engine) throws Exception;
    void input(Window window);
    void update(float interval);
    void lateInit();
    void startNewScene(Scene scene);
    public Camera getCamera();

    public Engine getEngine();
    public  float getMouseSensitivity();
    public  float getMoveSpeed();
}
