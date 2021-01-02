package paul.coeus;

import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;
import paul.coeus.graphics.Material.Texture;
import paul.coeus.graphics.postProcessing.FBO;

public class GlobalModules {
    private static Window window;
    private static Camera camera;
    private static Texture tx;
    private static FBO fbo;


    public static FBO getFbo() {
        return fbo;
    }

    public static void setFbo(FBO fbo) {
        GlobalModules.fbo = fbo;
    }

    public static Texture getTx() {
        return tx;
    }

    public static void setTx(Texture tx) {
        GlobalModules.tx = tx;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static void setCamera(Camera camera) {
        GlobalModules.camera = camera;
    }

    public static Window getWindow() {
        return window;
    }

    public static void setWindow(Window window) {
        GlobalModules.window = window;
    }
}
