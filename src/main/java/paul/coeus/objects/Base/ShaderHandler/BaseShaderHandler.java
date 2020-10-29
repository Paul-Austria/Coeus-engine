package paul.coeus.objects.Base.ShaderHandler;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import paul.coeus.graphics.Material.Lights.DirectionalLight;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

public class BaseShaderHandler implements  IShaderHandler {
    ShaderProgram shaderProgram;

    @Override
    public Class getClassType() {
        return GameObject.class;
    }
    @Override
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public void setupUniforms() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/object_shader/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/object_shader/fragment.fs"));
        shaderProgram.link();

        // projection Matrix
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightListUniform("pointLights", 20);

        shaderProgram.createDirectionalLightUniform("directionalLight");
    }

    @Override
    public void setGlobalUniforms(Matrix4f projectionMatrix) {
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        shaderProgram.setUniform("texture_sampler", 0);
    }

    @Override
    public void RenderLights(Matrix4f viewMatrix, PointLight[] pointLights, DirectionalLight directionalLight,  Vector3f ambientLight, float specularPower) {

        shaderProgram.bind();

        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);

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
            shaderProgram.setUniform("pointLights", currPointLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shaderProgram.setUniform("directionalLight", currDirLight);

        shaderProgram.unbind();
    }
}
