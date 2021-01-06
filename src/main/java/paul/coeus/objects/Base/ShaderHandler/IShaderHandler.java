package paul.coeus.objects.Base.ShaderHandler;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import paul.coeus.objects.Base.Lights.DirectionalLight;
import paul.coeus.objects.Base.Lights.PointLight;
import paul.coeus.utils.ShaderProgram;

public interface IShaderHandler {


    public Class getClassType();
    public ShaderProgram getShaderProgram();
    public void setupUniforms() throws Exception;
    public void setGlobalUniforms(Matrix4f projectionMatrix);
    public void RenderLights(Matrix4f viewMatrix, PointLight[] pointLights, DirectionalLight directionalLight, Vector3f ambientLight, float specularPower);
}
