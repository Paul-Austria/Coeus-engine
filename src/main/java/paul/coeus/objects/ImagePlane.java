package paul.coeus.objects;

import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.Textures.Animation;
import paul.coeus.objects.Base.GameObject;

import java.util.HashMap;
import java.util.Map;

public class ImagePlane extends GameObject {

    Animation currentAni;
    Boolean isAnimating = true;
    Map<String, Animation> AnimationMap;
    public ImagePlane(int imageWidth, int imageHeight, float devider)
    {
        super();
        AnimationMap = new HashMap<>();
        float[] pos = new float[]{
                 0, imageHeight/devider,0,
                 0,0,0,
                imageWidth/devider,0,0,
                imageWidth/devider,imageHeight/devider,0
        };
        int[] indices = new int[] {
                // Front face
                1,0,3,3,2,1
        };
        float[] textures = new float[]{

            1,0,
            1,1,
            0,1,
            0,0
         };
        float[] normals = new float[12];
        Mesh mesh = new Mesh(pos, textures, indices, normals, null);
        super.setMesh(mesh);
    }

    public Boolean getAnimating() {
        return isAnimating;
    }

    public void setAnimating(Boolean animating) {
        isAnimating = animating;
    }



    public void setAnimation(String AnimationName, Animation ani)
    {
        AnimationMap.put(AnimationName, ani);
    }

    public void useAnimation(String name)
    {
        currentAni = AnimationMap.get(name);
    }

    public void updateAnimation(float timepassed)
    {
        if(isAnimating)getMesh().setTexture( currentAni.getNextTexture(timepassed));
    }

}
