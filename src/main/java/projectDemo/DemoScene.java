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

    @Override
    public void setupUI() {
        Button button = new Button("Add components", 20, 20, 160, 30);

        button.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button.isPressed()) {
                JFileChooser chooser = new JFileChooser();
                // Dialog zum Oeffnen von Dateien anzeigen
                int rt = chooser.showOpenDialog(null);

                if(rt == JFileChooser.APPROVE_OPTION)
                {
                    Mesh m = null;
                    try {
                        m = LoadObjects.loadOBJ(chooser.getSelectedFile().getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        m.setTexture(new Texture("src/main/Texture/grassblock.png"));
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
        GlobalModules.getWindow().getFrame().getContainer().add(button);



        Button button2 = new Button("x+", 0, 120, 20, 20);

        button2.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button2.isPressed()) {
                    gameObject.getPosition().x += 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(button2);

        Button button3 = new Button("x-", 0, 140, 20, 20);

        button3.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button3.isPressed()) {
                gameObject.getPosition().x -= 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(button3);

        Button button4 = new Button("y+", 0, 160, 20, 20);

        button4.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button4.isPressed()) {
                gameObject.getPosition().y += 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(button4);


        Button button5 = new Button("y-", 0, 180, 20, 20);

        button5.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button5.isPressed()) {
                gameObject.getPosition().y -= 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(button5);

        Button button6 = new Button("z+", 0, 200, 20, 20);

        button6.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button6.isPressed()) {
                gameObject.getPosition().z += 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(button6);


        Button button7 = new Button("z-", 0, 220, 20, 20);

        button7.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button7.isPressed()) {
                gameObject.getPosition().z -= 1;
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(button7);
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
