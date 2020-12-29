package paul.coeus.graphics.Material;

import org.joml.Vector4f;
import org.w3c.dom.Text;

public class Material {
    final Vector4f dColour = new Vector4f(1.0f, 1, 1, 1.0f);
    private Vector4f ambientC;
    private Vector4f diffuseC;
    private Vector4f specularC;


    private Texture texture;
    private Texture normalMap;

    private float reflectance = 1;
    private boolean useTexture;
    public Material(Texture texture) {
        this.texture = texture;

        ambientC = dColour;
        diffuseC  = dColour;
        specularC = dColour;
    }

    public Material() {
        ambientC = dColour;
        diffuseC  = dColour;
        specularC = dColour;
    }


    public Vector4f getdColour() {
        return dColour;
    }

    public Vector4f getAmbientC() {
        return ambientC;
    }

    public void setAmbientC(Vector4f ambientC) {
        this.ambientC = ambientC;
    }

    public Vector4f getDiffuseC() {
        return diffuseC;
    }

    public void setDiffuseC(Vector4f diffuseC) {
        this.diffuseC = diffuseC;
    }

    public Vector4f getSpecularC() {
        return specularC;
    }

    public void setSpecularC(Vector4f specularC) {
        this.specularC = specularC;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isUseTexture() {
        return useTexture;
    }

    public void setUseTexture(boolean useTexture) {
        this.useTexture = useTexture;
    }

    public boolean hasNormalMap()
    {
        return this.normalMap != null;
    }
    public Texture getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public boolean isTextured() {
        return texture != null;
    }

    public float getReflectance() {
        return reflectance;
    }
}
