package paul.coeus;

import org.joml.Matrix4f;
import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;
import paul.coeus.graphics.graphUtils.Transformation;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Matrix4f projectionMatrix;

    private Transformation transformation;
    private ShaderProgram shaderProgram;

    public Renderer(){
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception{
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shaderProgram.link();

        // projection Matrix
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");

  //      window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }


    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, GameObject[] gameObjects, Camera camera) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);


        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);
        // Draw the mesh
        for (GameObject gameObject : gameObjects)
        {
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            shaderProgram.setUniform("colour", gameObject.getMesh().getColour());
            shaderProgram.setUniform("useColour", gameObject.getMesh().isTextured() ? 0 : 1);
            gameObject.getMesh().render();
        }

        shaderProgram.unbind();
    }


    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

    }

    private void glDisableVertexAttribArray(int i) {
    }
}
