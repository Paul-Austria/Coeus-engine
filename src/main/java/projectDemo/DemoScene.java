package projectDemo;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.image.FBOImage;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style;
import paul.coeus.Engine;
import paul.coeus.GlobalModules;
import paul.coeus.base.IGameLogic;
import paul.coeus.base.IScene;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.Material.Texture;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.utils.LoadObjects;

import java.awt.*;

public class DemoScene implements IScene {
    IGameLogic gameLogic;
    GameObject gameObject;

    int i;
    @Override
    public void setupUI() {
        Button button = new Button("Add components", 20, 20, 160, 30);

        button.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if(button.isPressed()) {
                i++;
                System.out.println("Button pressed" + i);
            }});
        GlobalModules.getWindow().getFrame().getContainer().add(button);
    }

    @Override
    public void init(IGameLogic gameLogic) throws Exception {



        this.gameLogic = gameLogic;
        Engine engine = gameLogic.getEngine();

        Mesh m = LoadObjects.loadOBJ("src/main/Objects/test.obj");
        m.setTexture(new Texture("src/main/Texture/grassblock.png"));
        m.getMaterial().setAmbientC(new Vector4f(1,0,0,1));
        gameObject = new GameObject(m);
        gameObject.getMesh().getMaterial().setReflectance(0.1f);
        engine.addGameObject(gameObject);
        gameObject.setPosition(0,0,-1);


        GameObject gameObject2 = new GameObject(m);
        gameObject2.getMesh().getMaterial().setReflectance(0.1f);
        engine.addGameObject(gameObject2);
        gameObject.setPosition(0,2,-1);
     //   engine.setSkyBox("src/main/Texture/skybox.png");

        Mesh m2 = LoadObjects.loadOBJ("src/main/Objects/quad.obj");
        m2.setTexture(new Texture("src/main/Texture/Rock/rock.png"));
        m2.setNormal(new Texture("src/main/Texture/Rock/rock_normals.png"));
        m2.getMaterial().setAmbientC(new Vector4f(1,0,0,1));
        m2.getMaterial().setReflectance(0.1f);
        GameObject gameObject1 = new GameObject(m2);
        gameObject1.setRotation(0, 0, 0);;
        gameObject1.setScale(10);
        gameObject1.setPosition(10,-3,0);
        engine.addGameObject(gameObject1);



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
        gameObject.getRotation().x += 0.5;
        gameObject.getRotation().y += 0.5;
        gameObject.getRotation().z += 0.5;

        GameObject x = gameLogic.getEngine().getSelected();
        if(x != null)
        {
            System.out.println(x);
        }
    }
}
