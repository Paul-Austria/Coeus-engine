package projectDemo;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.event.KeyEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.KeyEventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.border.SimpleLineBorder;
import org.liquidengine.legui.style.color.ColorConstants;
import org.lwjgl.glfw.GLFW;
import paul.coeus.Engine;
import paul.coeus.GlobalModules;
import paul.coeus.base.IGameLogic;
import paul.coeus.base.IScene;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.Material.Animation;
import paul.coeus.graphics.Material.Texture;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.Base.ShaderHandler.SkyBoxShaderHandler;
import paul.coeus.objects.Base.SkyBox;
import paul.coeus.objects.ImagePlane;
import paul.coeus.utils.LoadObjects;

import javax.swing.*;
import java.awt.*;

import static org.liquidengine.legui.component.optional.align.HorizontalAlign.*;

public class DemoScene implements IScene {
    IGameLogic gameLogic;
    int i;
    private GameObject gameObject;
    int pos = 0;
    Mesh m = null;

    @Override
    public void setupUI() {
        Engine engine = gameLogic.getEngine();

        Button addComponent = new Button("Add components", 20, 20, 160, 30);

        addComponent.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(addComponent.isPressed()) {
                JFileChooser chooser = new JFileChooser();
                // Dialog zum Oeffnen von Dateien anzeigen
                int rt = chooser.showOpenDialog(null);

                if(rt == JFileChooser.APPROVE_OPTION)
                {
                    try {
                        m = LoadObjects.loadOBJ(chooser.getSelectedFile().getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    m.getMaterial().setAmbientC(new Vector4f(1,0,0,1));
                    gameObject = new GameObject(m);
                    gameObject.getMesh().getMaterial().setReflectance(0.1f);
                    gameLogic.getEngine().addGameObject(gameObject);
                    gameObject.setPosition(0,0,-0);
                }
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(addComponent);

        Button changeTexture = new Button("Change texture", 20, 60, 160, 30);

        changeTexture.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(changeTexture.isPressed()) {
                JFileChooser chooser = new JFileChooser();
                int rt = chooser.showOpenDialog(null);

                if(rt == JFileChooser.APPROVE_OPTION)
                {
                    try {
                        m.setTexture(new Texture(chooser.getSelectedFile().getPath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(changeTexture);

        Button positiveX = new Button("x+", 0, 120, 20, 20);

        positiveX.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(positiveX.isPressed()) {
                    gameObject.getPosition().x += 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(positiveX);

        Button negativeX = new Button("x-", 0, 140, 20, 20);

        negativeX.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(negativeX.isPressed()) {
                gameObject.getPosition().x -= 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(negativeX);

        Button positiveY = new Button("y+", 0, 160, 20, 20);

        positiveY.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(positiveY.isPressed()) {
                gameObject.getPosition().y += 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(positiveY);

        Button negativeY = new Button("y-", 0, 180, 20, 20);

        negativeY.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(negativeY.isPressed()) {
                gameObject.getPosition().y -= 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(negativeY);

        Button positiveZ = new Button("z+", 0, 200, 20, 20);

        positiveZ.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(positiveZ.isPressed()) {
                gameObject.getPosition().z += 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(positiveZ);

        Button negativeZ = new Button("z-", 0, 220, 20, 20);

        negativeZ.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(negativeZ.isPressed()) {
                gameObject.getPosition().z -= 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(negativeZ);

        Button changeLastObj = new Button(">", 0, 240, 20, 20);

        changeLastObj.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(changeLastObj.isPressed()) {
                pos++;
                if (pos >= engine.getGameObjects().size()) {
                    pos = 0;
                }
                gameObject = engine.getGameObjects().get(pos);
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(changeLastObj);
    }

    @Override
    public void init(IGameLogic gameLogic) throws Exception {
        this.gameLogic = gameLogic;
        Engine engine = gameLogic.getEngine();

        PointLight[] pointLights = new  PointLight[]{
                new PointLight(new Vector3f(1, 1, 1),new Vector3f(10, -2, 5), 1f )
        };
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 0.1f);
        pointLights[0].setAttenuation(att);
        pointLights[0].setLightCube();

        for (PointLight p:
             pointLights) {
            engine.addPointLight(p);
        }

    }

    @Override
    public void update(float interval) {
        if(gameObject != null)
        {
        }
    }
}
