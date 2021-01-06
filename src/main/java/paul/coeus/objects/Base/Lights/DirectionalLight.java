package paul.coeus.objects.Base.Lights;

import org.joml.Vector3f;

public class DirectionalLight {
    private Vector3f color;

    private Vector3f direction = new Vector3f(0,0,0);

    private float intensity;

    private float angle;

    public DirectionalLight(Vector3f color, float intensity, float angle) {
        this.color = color;
        this.intensity = intensity;
        setAngel(angle);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight light) {
        this(new Vector3f(light.getColor()), light.getIntensity(), light.angle);
    }

    public void setAngel(float lightAngle)
    {
        if (lightAngle > 90) {
            this.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float)(Math.abs(lightAngle) - 80)/ 10.0f;
            this.setIntensity(factor);
            this.getColor().y = Math.max(factor, 0.9f);
            this.getColor().z = Math.max(factor, 0.5f);
        } else {
            this.setIntensity(1);
            this.getColor().x = 1;
            this.getColor().y = 1;
            this.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        this.getDirection().x = (float) Math.sin(angRad);
        this.getDirection().y = (float) Math.cos(angRad);
    }
}