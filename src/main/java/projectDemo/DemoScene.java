package projectDemo;

import paul.coeus.Engine;
import paul.coeus.base.IGameLogic;
import paul.coeus.base.Scene;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.Textures.Animation;
import paul.coeus.graphics.Textures.Texture;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.ImagePlane;
import paul.coeus.utils.LoadObjects;

public class DemoScene implements Scene {
    IGameLogic gameLogic;
    @Override
    public void init(IGameLogic gameLogic) throws Exception {
        this.gameLogic = gameLogic;
        Engine engine = gameLogic.getEngine();
            Mesh m = LoadObjects.loadOBJ("src/main/Objects/test.obj");
            m.setTexture(new Texture("src/main/Texture/grassblock.png"));
            GameObject gameObject = new GameObject(m);
            engine.addGameObject(gameObject);

            ImagePlane im = new ImagePlane(16,31, 100);
            im.setPosition(3,0,0);
            try {
            Texture[] texturel = new Texture[]{
                    new Texture("src/main/Texture/MarioAnimation/maria1.png"),
                    new Texture("src/main/Texture/MarioAnimation/maria2.png"),
                    new Texture("src/main/Texture/MarioAnimation/maria3.png")
                };
                im.setAnimation("Standard",new Animation(texturel, 5));
                im.useAnimation("Standard");
            } catch (Exception e) {
                e.printStackTrace();
            }
            im.setScale(4);
            engine.addGameObject(im);
    }

    @Override
    public void update(float interval) {

    }
}
