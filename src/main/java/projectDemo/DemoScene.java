package projectDemo;

import org.joml.Vector3f;
import org.joml.Vector4f;
import paul.coeus.Engine;
import paul.coeus.base.IGameLogic;
import paul.coeus.base.IScene;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.Material.Animation;
import paul.coeus.graphics.Material.Texture;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.ImagePlane;
import paul.coeus.utils.LoadObjects;

public class DemoScene implements IScene {
    IGameLogic gameLogic;
    GameObject gameObject;
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



            // Mario
            ImagePlane im = new ImagePlane(16,31, 100, true);
            im.setPosition(-0,-1.5f,2);
            try {
            Texture[] texturel = new Texture[]{
                    new Texture("src/main/Texture/MarioAnimation/maria1.png"),
                    new Texture("src/main/Texture/MarioAnimation/maria2.png"),
                    new Texture("src/main/Texture/MarioAnimation/maria3.png")
                };
                im.setAnimation("Standard",new Animation(texturel, 7));
                im.useAnimation("Standard");
            } catch (Exception e) {
                e.printStackTrace();
            }
            im.setScale(4);
            im.getMesh().getMaterial().setReflectance(0.1f);
            engine.addGameObject(im);

        PointLight[] pointLights = new  PointLight[]{
                new PointLight(new Vector3f(1, 1, 1),new Vector3f(2, -2, 2), 1 ),
                new PointLight(new Vector3f(1, 1, 1),new Vector3f(2, 2, 2), 1 ),
                new PointLight(new Vector3f(1, 1, 1),new Vector3f(-2, -2, 2), 1 ),
                new PointLight(new Vector3f(1, 1, 1),new Vector3f(2, -2, -2), 1 )
        };

        pointLights[0].setColor(new Vector3f(1,0,0));
        pointLights[2].setColor(new Vector3f(1,1,0));
        pointLights[3].setColor(new Vector3f(0,1,0));
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 0.1f);
        pointLights[0].setAttenuation(att);
        pointLights[0].setLightCube();
        pointLights[1].setLightCube();
        pointLights[2].setLightCube();
        pointLights[3].setLightCube();

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
    }
}
