package paul.coeus.graphics.postProcessing;

import org.apache.commons.lang3.ObjectUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import paul.coeus.GlobalModules;
import paul.coeus.graphics.Material.Texture;
import paul.coeus.objects.Base.ShaderHandler.IShaderHandler;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FBO {

    private  int id;
    private Texture texture;
    private FBOShaderHandler shaderHandler;
    FBOMesh mesh;
    int depthBuffer ;
    public FBO() throws Exception {

        glEnable(GL_DEPTH_TEST);
        id = glGenFramebuffers();

        texture = new Texture(GlobalModules.getWindow().getWidth(), GlobalModules.getWindow().getHeight());


        glBindFramebuffer(GL_FRAMEBUFFER, id);
        GL11.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.getId(), 0);




        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new Exception("Could not create FrameBuffer");
        }


        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        float pos[] = {

                -1,  1, 0.0f,
                -1, -1, 0.0f,
                1, -1, 0.0f,
                1,  1, 0.0f,
        };
        float textureCoords[] = {

                0,1,
                0,0,
                1,0,
                1,1


        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };

        shaderHandler = new FBOShaderHandler();
        shaderHandler.setupUniforms();
        mesh = new FBOMesh(pos, textureCoords, indices, texture);

    }

    public void render(){
        mesh.render();
    }

    public IShaderHandler getShaderHandler() {
        return shaderHandler;
    }

    public void setShaderHandler(FBOShaderHandler shaderHandler) {
        this.shaderHandler = shaderHandler;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getId() {
        return id;
    }
}