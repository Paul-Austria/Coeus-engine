package paul.coeus;

import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;

public class GlobalModules {
    private static Window window;
    private static Camera camera;

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
