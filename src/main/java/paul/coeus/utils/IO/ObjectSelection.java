package paul.coeus.utils.IO;

import org.joml.*;
import paul.coeus.GlobalModules;
import paul.coeus.Renderer;
import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;
import paul.coeus.graphics.graphUtils.Transformation;
import paul.coeus.objects.Base.GameObject;

public class ObjectSelection {
    private final Matrix4f invProjectionMatrix;
    private final Matrix4f invViewMatrix;
    private final Vector3f mouseDir;
    private final Vector4f tmpVec;

    private Transformation transformation;

    public ObjectSelection() {

        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        mouseDir = new Vector3f();
        tmpVec = new Vector4f();
        transformation = new Transformation();
    }

    public GameObject selectGameItem(GameObject[] gameItems, Window window, Vector2d mousePos, Camera camera) {
        // Transform mouse coordinates into normalized spaze [-1, 1]
        int wdwWitdh = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = (float)(2 * mousePos.x) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (float)(2 * mousePos.y) / (float)wdwHeight;
        float z = -1.0f;
        invProjectionMatrix.set(transformation.getProjectionMatrix(Renderer.getFOV(), wdwWitdh, wdwHeight, Renderer.getzNear(), Renderer.getzFar()));
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = GlobalModules.getRenderer().getTransformation().getViewMatrix(camera);
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);

        return selectGameItem(gameItems, camera.getPosition(), mouseDir);
    }


    private GameObject selectGameItem(GameObject[] gameItems, Vector3f center, Vector3f dir) {
        GameObject selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        Vector3f min = new Vector3f();
        Vector3f max = new Vector3f();
        Vector2f nearFar = new Vector2f();
        for (GameObject gameItem : gameItems) {
            min.set(gameItem.getPosition());
            max.set(gameItem.getPosition());
            min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
            max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance && gameItem.isVisible()) {
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }

        if (selectedGameItem != null) {
            return selectedGameItem;
        }

        return null;
    }
}
