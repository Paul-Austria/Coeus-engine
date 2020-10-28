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
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

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
    public Renderer(){
        specularPower = 0.1f;
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception{

         setupBaseObjectShader();
         setupLightObjectShader();;
         //      window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

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





    public void render(Window window, GameObject[] gameObjects, PointLight[] pointLights,Camera camera) {
        clear();
        directionalLight.setAngel(directionalLight.getAngle()+0.9f);

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        RenderBaseObjects(viewMatrix, pointLights, window, gameObjects, projectionMatrix);
        RenderLightObjects(viewMatrix, pointLights, projectionMatrix);
    }


    public void RenderBaseObjects(Matrix4f viewMatrix, PointLight[] pointLights, Window window, GameObject[] gameObjects, Matrix4f projectionMatrix){
        baseObjectShader.bind();
        RenderLights(viewMatrix, pointLights, directionalLight);

        baseObjectShader.setUniform("projectionMatrix", projectionMatrix);
        baseObjectShader.setUniform("texture_sampler", 0);


        // Draw the mesh
        for (GameObject gameObject : gameObjects)
        {
            gameObject.setLocalUniforms(baseObjectShader, viewMatrix, transformation);
            gameObject.getMesh().render();
        }
        baseObjectShader.unbind();
    }

    public void RenderLights(Matrix4f viewMatrix,PointLight[] pointLights, DirectionalLight directionalLight){

        baseObjectShader.setUniform("ambientLight", ambientLight);
        baseObjectShader.setUniform("specularPower", specularPower);

        int numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLights[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            baseObjectShader.setUniform("pointLights", currPointLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        baseObjectShader.setUniform("directionalLight", currDirLight);
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
