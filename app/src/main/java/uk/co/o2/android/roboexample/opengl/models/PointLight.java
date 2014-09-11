package uk.co.o2.android.roboexample.opengl.models;

import org.jetbrains.annotations.NotNull;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class PointLight {

    private float[] mLightAmbient = new float[]{1.0f, 1.0f, 1.0f};
    private float[] mLightDiffuse = new float[]{1.0f, 1.0f, 1.0f};
    private float[] mLightSpecular = new float[]{1.0f, 1.0f, 1.0f};
    private float mSpecularShininess = 5;

    private Vector3 mPosition = new Vector3(0.0f, 0.0f, 0.0f);

    public float[] getAmbientColor() {
        return mLightAmbient;
    }

    public void setAmbientColor(@NotNull float[] ambientColor) {
        this.mLightAmbient = ambientColor;
    }

    public float[] getDiffuseColor() {
        return mLightDiffuse;
    }

    public void setDiffuseColor(@NotNull float[] diffuseColor) {
        this.mLightDiffuse = diffuseColor;
    }

    public float[] getSpecularColor() {
        return mLightSpecular;
    }

    public void setSpecularColor(@NotNull float[] specularColor) {
        this.mLightSpecular = specularColor;
    }

    public void setSpecularShininess(float specularShininess) {
        this.mSpecularShininess = specularShininess;
    }

    public float getSpecularShininess() {
        return mSpecularShininess;
    }


    public Vector3 getPosition() {
        return mPosition;
    }

    public void setPosition(@NotNull Vector3 position) {
        this.mPosition = position;
    }

    public void setPosition(float x, float y, float z) {
        this.mPosition.x = x;
        this.mPosition.y = y;
        this.mPosition.z = z;
    }
}
