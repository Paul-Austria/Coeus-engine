package paul.coeus.objects.Base.Lights;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import paul.coeus.graphics.Mesh;
import paul.coeus.graphics.graphUtils.Transformation;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.objects.Base.ShaderHandler.LightShaderHandler;
import paul.coeus.utils.LoadObjects;
import paul.coeus.utils.ShaderProgram;

public class PointLight extends GameObject{
    private Vector3f color;

    private Vector3f position;

    protected float intensity;

    private Attenuation attenuation;

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        super();
        setVisible(false);
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(), pointLight.getAttenuation());
    }

    @Override
    public void setLocalUniforms(ShaderProgram shaderProgram, Matrix4f viewMatrix, Transformation transformation){
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(this, viewMatrix);
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        shaderProgram.setUniform("colour", new Vector4f(getColor(), getIntensity()));
    }

    public void setLightCube(){
        try {
            Mesh m = LoadObjects.loadOBJ("src/main/Objects/test.obj");
            m.getMaterial().setAmbientC(new Vector4f(color, intensity));
            setShaderHandler(new LightShaderHandler());
             setMesh(m);
            setScale(0.2f);
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void removeLightCube(){
        setVisible(false);
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        if(isVisible())getMesh().getMaterial().setAmbientC(new Vector4f(color, intensity));
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        if(isVisible())getMesh().getMaterial().setAmbientC(new Vector4f(color, intensity));
        this.intensity = intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public GameObject getGameObject() {
        return this;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public static class Attenuation {

        private float constant;

        private float linear;

        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public float getLinear() {
            return linear;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }

        public float getExponent() {
            return exponent;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }


}
