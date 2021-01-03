package paul.coeus.graphics.postProcessing;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import paul.coeus.graphics.Material.Lights.DirectionalLight;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.objects.Base.ShaderHandler.IShaderHandler;
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

public class FBOShaderHandler implements IShaderHandler {
    ShaderProgram shaderProgram;

    @Override
    public Class getClassType() {
        return FBOShaderHandler.class;
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public void setupUniforms() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/FBO.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/FBO.fs"));
        shaderProgram.link();


    }

    @Override
    public void setGlobalUniforms(Matrix4f projectionMatrix) {

    }

    @Override
    public void RenderLights(Matrix4f viewMatrix, PointLight[] pointLights, DirectionalLight directionalLight, Vector3f ambientLight, float specularPower) {

    }
}
