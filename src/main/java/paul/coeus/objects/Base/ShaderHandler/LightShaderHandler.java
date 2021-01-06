package paul.coeus.objects.Base.ShaderHandler;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import paul.coeus.objects.Base.Lights.DirectionalLight;
import paul.coeus.objects.Base.Lights.PointLight;
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

public class LightShaderHandler implements IShaderHandler {
    ShaderProgram shaderProgram;
    @Override
    public Class getClassType() {
        return LightShaderHandler.class;
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public void setupUniforms() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/Point_Light_Shader/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/Point_Light_Shader/fragment.fs"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("colour");
    }

    @Override
    public void setGlobalUniforms(Matrix4f projectionMatrix) {
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
    }

    @Override
    public void RenderLights(Matrix4f viewMatrix, PointLight[] pointLights, DirectionalLight directionalLight, Vector3f ambientLight, float specularPower) {

    }
}
