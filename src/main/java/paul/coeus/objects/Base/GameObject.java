package paul.coeus.objects.Base;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.graphUtils.Transformation;
import paul.coeus.objects.Base.ShaderHandler.BaseShaderHandler;
import paul.coeus.objects.Base.ShaderHandler.IShaderHandler;
import paul.coeus.utils.ShaderProgram;

public class GameObject {
    private Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;

    public GameObject(){
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }
    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void setLocalUniforms(ShaderProgram shaderProgram, Matrix4f viewMatrix, Transformation transformation){
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(this, viewMatrix);
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        shaderProgram.setUniform("material", getMesh().getMaterial());
    }

    public void update()
    {

    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public IShaderHandler getShader() {
        return new BaseShaderHandler();
    }
}
