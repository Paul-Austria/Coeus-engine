package paul.coeus.graphics.Material.Lights;

import org.joml.Vector3f;
import org.joml.Vector4f;
import paul.coeus.graphics.Mesh;
import paul.coeus.objects.Base.GameObject;
import paul.coeus.utils.LoadObjects;

public class PointLight{
    private Vector3f color;

    private Vector3f position;

    protected float intensity;

    private Attenuation attenuation;

    GameObject gameObject;

    public PointLight(Vector3f color, Vector3f position, float intensity) {
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

    public void setLightCube(){
        try {
            Mesh m = LoadObjects.loadOBJ("src/main/Objects/test.obj");
            m.getMaterial().setAmbientC(new Vector4f(color, intensity));
            gameObject = new GameObject(m);
            gameObject.setScale(0.2f);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void removeLightCube(){
        gameObject = null;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        if(gameObject != null)gameObject.getMesh().getMaterial().setAmbientC(new Vector4f(color, intensity));
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
        if(gameObject != null)gameObject.getMesh().getMaterial().setAmbientC(new Vector4f(color, intensity));
        this.intensity = intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public GameObject getGameObject() {
        return gameObject;
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
