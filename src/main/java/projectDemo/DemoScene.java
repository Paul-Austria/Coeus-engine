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
import paul.coeus.objects.Base.ShaderHandler.SkyBoxShaderHandler;
import paul.coeus.objects.Base.SkyBox;
import paul.coeus.objects.ImagePlane;
import paul.coeus.utils.LoadObjects;

public class DemoScene implements IScene {
    IGameLogic gameLogic;
    @Override
    public void init(IGameLogic gameLogic) throws Exception {
        this.gameLogic = gameLogic;
        Engine engine = gameLogic.getEngine();

            Mesh m2 = LoadObjects.loadOBJ("src/main/Objects/quad.obj");
            m2.setTexture(new Texture("src/main/Texture/Rock/rock.png"));
            m2.setNormal(new Texture("src/main/Texture/Rock/rock_normals.png"));
            m2.getMaterial().setAmbientC(new Vector4f(1,0,0,1));
            m2.getMaterial().setReflectance(0.1f);
            GameObject gameObject1 = new GameObject(m2);
            gameObject1.setRotation(0, 0, 0);;
            gameObject1.setScale(10);
            gameObject1.setPosition(10,-3,0);
;
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
    }
}
