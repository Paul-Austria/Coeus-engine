package paul.coeus;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import paul.coeus.base.Window;
import paul.coeus.graphics.Camera;
import paul.coeus.graphics.Material.Lights.DirectionalLight;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.graphics.graphUtils.Transformation;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.Base.ShaderHandler.BaseShaderHandler;
import paul.coeus.objects.Base.ShaderHandler.IShaderHandler;
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;


public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private static final int MAX_POINT_LIGHTS = 20;

    private Matrix4f projectionMatrix;
    private float specularPower;
    private Transformation transformation;
    private ShaderProgram baseObjectShader;
    private  ShaderProgram lightObjectShader;

    List<IShaderHandler> customShaders = new ArrayList<>();

    public Renderer(){
        specularPower = 0.1f;
        transformation = new Transformation();
    }

    public void init(Window window, List<IShaderHandler> shaderHandlers) throws Exception{
        setupBaseObjectShader();
        setupLightObjectShader();;
        setupCustomShaders();
        setupShaders(shaderHandlers);

         //      window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

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

    private void setupLightObjectShader() throws  Exception{
        lightObjectShader = new ShaderProgram();
        lightObjectShader.createVertexShader(Utils.loadResource("/shaders/Point_Light_Shader/vertex.vs"));
        lightObjectShader.createFragmentShader(Utils.loadResource("/shaders/Point_Light_Shader/fragment.fs"));
        lightObjectShader.link();

        lightObjectShader.createUniform("projectionMatrix");
        lightObjectShader.createUniform("modelViewMatrix");
        lightObjectShader.createUniform("colour");
    }
    private void setupBaseObjectShader() throws Exception {
        baseObjectShader = new ShaderProgram();
        baseObjectShader.createVertexShader(Utils.loadResource("/shaders/object_shader/vertex.vs"));
        baseObjectShader.createFragmentShader(Utils.loadResource("/shaders/object_shader/fragment.fs"));
        baseObjectShader.link();

        // projection Matrix
        baseObjectShader.createUniform("projectionMatrix");
        baseObjectShader.createUniform("modelViewMatrix");
        baseObjectShader.createUniform("texture_sampler");
        // Create uniform for material
        baseObjectShader.createMaterialUniform("material");
        // Create lighting related uniforms
        baseObjectShader.createUniform("specularPower");
        baseObjectShader.createUniform("ambientLight");
        baseObjectShader.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);

        baseObjectShader.createDirectionalLightUniform("directionalLight");
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

        RenderOtherObjects(viewMatrix, pointLights, window, gameObjects,shaders, projectionMatrix);
        RenderLightObjects(viewMatrix, pointLights, projectionMatrix);
    }

    private void RenderOtherObjects(Matrix4f viewMatrix, PointLight[] pointLights, Window window, HashMap<Class, List<GameObject>> gameObjects,List<IShaderHandler> shaders, Matrix4f projectionMatrix) {
        for (IShaderHandler shader: shaders) {
            shader.getShaderProgram().bind();
            shader.setGlobalUniforms(projectionMatrix);
            List<GameObject> toRender = gameObjects.get(shader.getClass());

            for (GameObject gameObject: toRender) {
                gameObject.setLocalUniforms(shader.getShaderProgram(), viewMatrix, transformation);
                gameObject.getMesh().render();
            }

            shader.getShaderProgram().unbind();
        }
    }



    public void RenderBaseObjects(Matrix4f viewMatrix, PointLight[] pointLights, Window window, GameObject[] gameObjects, Matrix4f projectionMatrix){
        baseObjectShader.bind();

        baseObjectShader.setUniform("projectionMatrix", projectionMatrix);
        baseObjectShader.setUniform("texture_sampler", 0);


        // Draw the mesh
        for (GameObject gameObject : gameObjects) {
                gameObject.setLocalUniforms(baseObjectShader, viewMatrix, transformation);                                                                      
                gameObject.getMesh().render();
        }                                                                                                                                                       
        baseObjectShader.unbind();                                                                                                                              
    }                                                                                                                                                           
                                                                                                                                                                
    public void RenderLights(Matrix4f viewMatrix,PointLight[] pointLights, DirectionalLight directionalLight, List<IShaderHandler> shaders){


        for (IShaderHandler shaderHandler: shaders) {
            shaderHandler.RenderLights(viewMatrix, pointLights, directionalLight, ambientLight, specularPower);
        }
    }
    public void RenderLightObjects(Matrix4f viewMatrix,PointLight[] pointLights, Matrix4f projectionMatrix)
    {


        lightObjectShader.bind();
        lightObjectShader.setUniform("projectionMatrix", projectionMatrix);
        for (PointLight pointLight : pointLights)
        {
            if(pointLight.getGameObject() != null)
            {
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(pointLight, viewMatrix);
                lightObjectShader.setUniform("modelViewMatrix", modelViewMatrix);

                lightObjectShader.setUniform("colour", new Vector4f(pointLight.getColor(), pointLight.getIntensity()));
                pointLight.getGameObject().getMesh().render();
            }
        }

        lightObjectShader.unbind();
    }

    public void cleanup() {
        if (baseObjectShader != null) {
            baseObjectShader.cleanup();
        }

    }

    private void glDisableVertexAttribArray(int i) {
    }
}
