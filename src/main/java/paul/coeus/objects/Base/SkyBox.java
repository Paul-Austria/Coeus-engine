package paul.coeus.objects.Base;

import org.joml.Matrix4f;
import paul.coeus.GlobalModules;
import paul.coeus.graphics.Material.Material;
import paul.coeus.graphics.Material.Texture;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.graphUtils.Transformation;
import paul.coeus.objects.Base.ShaderHandler.SkyBoxShaderHandler;
import paul.coeus.utils.LoadObjects;
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

public class SkyBox extends GameObject {

    public SkyBox(String texture)
    {
        super();
        this.setShaderHandler(new SkyBoxShaderHandler());
        try {
            Mesh skyBoxMesh = LoadObjects.loadOBJFromString("// Front Face\n" +
                    "v -1.000000 1.000000 1.000000\n" +
                    "v -1.000000 -1.000000 1.000000\n" +
                    "v 1.000000 -1.000000 1.000000\n" +
                    "v 1.000000 1.000000 1.000000\n" +
                    "\n" +
                    "// Left Face\n" +
                    "v -1.000000 1.000000 1.000000\n" +
                    "v -1.000000 -1.000000 1.000000\n" +
                    "v -1.000000 -1.000000 -1.000000\n" +
                    "v -1.000000 1.000000 -1.000000\n" +
                    "\n" +
                    "// Right Face\n" +
                    "v 1.000000 1.000000 1.000000\n" +
                    "v 1.000000 -1.000000 1.000000\n" +
                    "v 1.000000 -1.000000 -1.000000\n" +
                    "v 1.000000 1.000000 -1.000000\n" +
                    "\n" +
                    "// Back Face\n" +
                    "v -1.000000 1.000000 -1.000000\n" +
                    "v -1.000000 -1.000000 -1.000000\n" +
                    "v 1.000000 -1.000000 -1.000000\n" +
                    "v 1.000000 1.000000 -1.000000\n" +
                    "\n" +
                    "// Top Face\n" +
                    "v -1.000000 1.000000 -1.000000\n" +
                    "v -1.000000 1.000000 1.000000\n" +
                    "v 1.000000 1.000000 1.000000\n" +
                    "v 1.000000 1.000000 -1.000000\n" +
                    "\n" +
                    "// Bottom Face\n" +
                    "v -1.000000 -1.000000 -1.000000\n" +
                    "v -1.000000 -1.000000 1.000000\n" +
                    "v 1.000000 -1.000000 1.000000\n" +
                    "v 1.000000 -1.000000 -1.000000\n" +
                    "\n" +
                    "// Front Face\n" +
                    "vt 0.333333 0.500000\n" +
                    "vt 0.333333 0.000000\n" +
                    "vt 0.000000 0.000000\n" +
                    "vt 0.000000 0.500000\n" +
                    "\n" +
                    "// Left Face\n" +
                    "vt 0.000000 1.000000\n" +
                    "vt 0.000000 0.500000\n" +
                    "vt 0.333333 0.500000\n" +
                    "vt 0.333333 1.000000\n" +
                    "\n" +
                    "// Right Face\n" +
                    "vt 1.000000 1.000000\n" +
                    "vt 1.000000 0.500000\n" +
                    "vt 0.666666 0.500000\n" +
                    "vt 0.666666 1.000000\n" +
                    "\n" +
                    "// Back Face\n" +
                    "vt 0.333333 1.000000\n" +
                    "vt 0.333333 0.500000\n" +
                    "vt 0.666666 0.500000\n" +
                    "vt 0.666666 1.000000\n" +
                    "\n" +
                    "// Top Face\n" +
                    "vt 0.340000 0.500000\n" +
                    "vt 0.666666 0.500000\n" +
                    "vt 0.666666 0.000000\n" +
                    "vt 0.340000 0.000000\n" +
                    "\n" +
                    "// Bottom Face\n" +
                    "vt 0.666666 0.500000\n" +
                    "vt 0.666666 0.000000\n" +
                    "vt 1.000000 0.000000\n" +
                    "vt 1.000000 0.500000\n" +
                    "\n" +
                    "// Front Face\n" +
                    "f 3/3/ 2/2/ 1/1/\n" +
                    "f 3/3/ 1/1/ 4/4/\n" +
                    "\n" +
                    "// Left Face\n" +
                    "f 5/5/ 6/6/ 7/7/\n" +
                    "f 8/8/ 5/5/ 7/7/\n" +
                    "\n" +
                    "// Right Face\n" +
                    "f 11/11/ 10/10/ 9/9/\n" +
                    "f 11/11/ 9/9/ 12/12/\n" +
                    "\n" +
                    "// Back Face\n" +
                    "f 13/13/ 14/14/ 15/15/\n" +
                    "f 16/16/ 13/13/ 15/15/\n" +
                    "\n" +
                    "// Top Face\n" +
                    "f 19/19/ 18/18/ 17/17/\n" +
                    "f 19/19/ 17/17/ 20/20/\n" +
                    "\n" +
                    "// Bottom Face\n" +
                    "f 21/21/ 22/22/ 23/23/\n" +
                    "f 24/24/ 21/21/ 23/23/");
            this.setScale(500);
            Texture skyBoxtexture = new Texture(texture);
            skyBoxMesh.setMaterial(new Material(skyBoxtexture));
            setMesh(skyBoxMesh);

            setPosition(0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public SkyBox(String obj ,String texture)
    {
        super();
        this.setShaderHandler(new SkyBoxShaderHandler());
        try {
            Mesh skyBoxMesh = LoadObjects.loadOBJ(obj);
            this.setScale(500);
            Texture skyBoxtexture = new Texture(texture);
            skyBoxMesh.setMaterial(new Material(skyBoxtexture));
            setMesh(skyBoxMesh);

            setPosition(0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void setLocalUniforms(ShaderProgram shaderProgram, Matrix4f viewMatrix, Transformation transformation){
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(this, viewMatrix);
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
    }

    @Override
    public void update(){
        this.setPosition(GlobalModules.getCamera().getPosition());
    }
}
