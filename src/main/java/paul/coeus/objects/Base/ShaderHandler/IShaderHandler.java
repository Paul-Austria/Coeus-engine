package paul.coeus.objects.Base.ShaderHandler;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import paul.coeus.graphics.Material.Lights.DirectionalLight;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.utils.ShaderProgram;

import java.awt.*;

public interface IShaderHandler {


    public Class getClassType();
    public ShaderProgram getShaderProgram();
    public void setupUniforms() throws Exception;
    public void setGlobalUniforms(Matrix4f projectionMatrix);
    public void RenderLights(Matrix4f viewMatrix, PointLight[] pointLights, DirectionalLight directionalLight, Vector3f ambientLight, float specularPower);
}
