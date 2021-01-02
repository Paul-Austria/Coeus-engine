package paul.coeus.graphics.postProcessing;

import org.lwjgl.opengl.GL11;
import paul.coeus.GlobalModules;
import paul.coeus.graphics.Material.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FBO {

    private  int id;
    private Texture texture;
    public FBO() throws Exception {
        // Create a FBO to render the depth map
        id = glGenFramebuffers();
        // Create the depth map texture
        texture = new Texture(GlobalModules.getWindow().getWidth(), GlobalModules.getWindow().getHeight());

        // Attach the the depth map texture to the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, id);
        GL11.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.getId(), 0);

        glDrawBuffer(GL_FRONT);
        glReadBuffer(GL_FRONT);


        // Set only depth


        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new Exception("Could not create FrameBuffer");
        }

        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }




    public Texture getTexture() {
        return texture;
    }

    public int getId() {
        return id;
    }
}