package paul.coeus.graphics.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import paul.coeus.GlobalModules;
import paul.coeus.graphics.Material.Texture;
import paul.coeus.objects.Base.ShaderHandler.IShaderHandler;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FBO {

    private  int id;
    private Texture colourTexture;
    private int depthTexture;
    private int depthBuffer;
    private FBOShaderHandler shaderHandler;
    FBOMesh mesh;
    public FBO() throws Exception {

        id = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, id);

        //COLOUR STUFF
        GL11.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        colourTexture = new Texture(GlobalModules.getWindow().getWidth(), GlobalModules.getWindow().getHeight());

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, colourTexture.getId(), 0);

        //create Depth
        depthTexture = createDepthTexture(GlobalModules.getWindow().getWidth(), GlobalModules.getWindow().getHeight());
        depthBuffer = createDepthBuffer(GlobalModules.getWindow().getWidth(), GlobalModules.getWindow().getHeight());



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
        mesh = new FBOMesh(pos, textureCoords, indices, colourTexture);

    }


    private int createDepthTexture(int width, int height)
    {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
        return texture;
    }

    private int createDepthBuffer(int width, int height){
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
                height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }

    public int getDepthTexture() {
        return depthTexture;
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

    public Texture getColourTexture() {
        return colourTexture;
    }

    public int getId() {
        return id;
    }
}