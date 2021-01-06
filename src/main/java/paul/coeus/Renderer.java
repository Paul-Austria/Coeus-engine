package paul.coeus;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;
import paul.coeus.objects.Base.Lights.DirectionalLight;
import paul.coeus.objects.Base.Lights.PointLight;
import paul.coeus.graphics.graphUtils.Transformation;
import paul.coeus.graphics.postProcessing.FBO;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.Base.ShaderHandler.IShaderHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;


public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private static final int MAX_POINT_LIGHTS = 20;

    private Matrix4f projectionMatrix;
    private float specularPower;
    private Transformation transformation;

    List<IShaderHandler> customShaders = new ArrayList<>();
    public Renderer(){
        specularPower = 0.1f;
        transformation = new Transformation();
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public void init(Window window, List<IShaderHandler> shaderHandlers) throws Exception{

        setupCustomShaders();
        setupShaders(shaderHandlers);
        GlobalModules.setFbo(new FBO());         //      window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    public void setupShaders(List<IShaderHandler> shaderHandlers) throws Exception {
        for (IShaderHandler shaderHandler: shaderHandlers) {
            shaderHandler.setupUniforms();
        }
    }

    private void setupCustomShaders() throws Exception {
        for(IShaderHandler shaderHandler : customShaders)
        {
            shaderHandler.setupUniforms();
        }
    }

    public static float getFOV() {
        return FOV;
    }

    public static float getzNear() {
        return Z_NEAR;
    }

    public static float getzFar() {
        return Z_FAR;
    }

    Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
    DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1,1,1),1, 0);
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, HashMap<Class, List<GameObject>>gameObjects , List<IShaderHandler> shaders, PointLight[] pointLights, Camera camera) {
        clear();
        directionalLight.setAngel(directionalLight.getAngle()+0.9f);

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        RenderLights(viewMatrix, pointLights, directionalLight, shaders);



        glBindFramebuffer(GL_FRAMEBUFFER, GlobalModules.getFbo().getId());

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        RenderObjects(viewMatrix, pointLights, window, gameObjects,shaders, projectionMatrix);
        //RenderLightObjects(viewMatrix, pointLights, projectionMatrix);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        if(window.isDebugMode() )
        {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        }
        renderFBO(projectionMatrix);
        if(window.isDebugMode())
        {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        }
    }

    private void renderFBO(Matrix4f projectionMatrix ) {
        GlobalModules.getFbo().getShaderHandler().getShaderProgram().bind();

        GlobalModules.getFbo().getShaderHandler().setGlobalUniforms(projectionMatrix);

        GlobalModules.getFbo().render();

        GlobalModules.getFbo().getShaderHandler().getShaderProgram().unbind();
    }

    private void RenderObjects(Matrix4f viewMatrix, PointLight[] pointLights, Window window, HashMap<Class, List<GameObject>> gameObjects, List<IShaderHandler> shaders, Matrix4f projectionMatrix) {
        for (IShaderHandler shader: shaders) {
            if(gameObjects.get(shader.getClass()) != null) {
                shader.getShaderProgram().bind();
                shader.setGlobalUniforms(projectionMatrix);
                List<GameObject> toRender = gameObjects.get(shader.getClass());

                for (GameObject gameObject : toRender) {

                    if(gameObject.isVisible()){
                        gameObject.setLocalUniforms(shader.getShaderProgram(), viewMatrix, transformation);
                        gameObject.getMesh().render();
                    }
                }

                shader.getShaderProgram().unbind();
            }
        }
    }

                                                                                                                                                                
    public void RenderLights(Matrix4f viewMatrix,PointLight[] pointLights, DirectionalLight directionalLight, List<IShaderHandler> shaders){


        for (IShaderHandler shaderHandler: shaders) {
            shaderHandler.RenderLights(viewMatrix, pointLights, directionalLight, ambientLight, specularPower);
        }
    }

    public void cleanup() {

        for (IShaderHandler shaderHandler : customShaders)
        {
            shaderHandler.getShaderProgram().cleanup();
        }

    }
}
