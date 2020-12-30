package paul.coeus.objects.Base.ShaderHandler;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import paul.coeus.graphics.Material.Lights.DirectionalLight;
import paul.coeus.graphics.Material.Lights.PointLight;
import paul.coeus.utils.ShaderProgram;
import paul.coeus.utils.Utils;

public class SkyBoxShaderHandler implements IShaderHandler {

    ShaderProgram skyBoxShaderProgram ;
    @Override
    public Class getClassType() {
        return SkyBoxShaderHandler.class;
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return skyBoxShaderProgram ;
    }

    @Override
    public void setupUniforms() throws Exception {
        skyBoxShaderProgram = new ShaderProgram();
        skyBoxShaderProgram.createVertexShader("#version 330\n" +
                "\n" +
                "layout (location=0) in vec3 position;\n" +
                "layout (location=1) in vec2 texCoord;\n" +
                "layout (location=2) in vec3 vertexNormal;\n" +
                "\n" +
                "out vec2 outTexCoord;\n" +
                "\n" +
                "uniform mat4 modelViewMatrix;\n" +
                "uniform mat4 projectionMatrix;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);\n" +
                "    outTexCoord = texCoord;\n" +
                "}");
        skyBoxShaderProgram.createFragmentShader("#version 330\n" +
                "\n" +
                "in vec2 outTexCoord;\n" +
                "in vec3 mvPos;\n" +
                "out vec4 fragColor;\n" +
                "\n" +
                "uniform sampler2D texture_sampler;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    fragColor = texture(texture_sampler, outTexCoord);\n" +
                "}");
        skyBoxShaderProgram.link();

        skyBoxShaderProgram.createUniform("projectionMatrix");
        skyBoxShaderProgram.createUniform("modelViewMatrix");
        skyBoxShaderProgram.createUniform("texture_sampler");
    }

    @Override
    public void setGlobalUniforms(Matrix4f projectionMatrix) {
        skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);
        skyBoxShaderProgram.setUniform("texture_sampler", 0);
    }

    @Override
    public void RenderLights(Matrix4f viewMatrix, PointLight[] pointLights, DirectionalLight directionalLight, Vector3f ambientLight, float specularPower) {

    }
}
