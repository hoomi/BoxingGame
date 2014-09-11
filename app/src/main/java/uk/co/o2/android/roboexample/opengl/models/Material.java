package uk.co.o2.android.roboexample.opengl.models;

import org.jetbrains.annotations.NotNull;


public class Material {
    private float[] mEmissive = new float[]{0.0f, 0.0f, 0.0f};
    private float[] mAmbient = new float[] {1.0f, 1.0f, 1.0f};
    private float[] mDiffuse = new float[] {1.0f, 1.0f, 1.0f};
    private float[] mSpecular = new float[] {1.0f, 1.0f, 1.0f};

    private float mSpecularShininess = 5.0f;
    private float mAlpha = 1.0f;


    public Material() {
    }

    public Material(@NotNull float[] mEmissive,
                    @NotNull float[] mAmbient,
                    @NotNull float[] mDiffuse,
                    @NotNull float[] mSpecular, float mSpecularShininess, float mAlpha) {
        this.mEmissive = mEmissive;
        this.mAmbient = mAmbient;
        this.mDiffuse = mDiffuse;
        this.mSpecular = mSpecular;
        this.mSpecularShininess = mSpecularShininess;
        this.mAlpha = mAlpha;
    }

    public void setEmissive(float r, float g, float b) {
        mEmissive[0] = r;
        mEmissive[1] = g;
        mEmissive[2] = b;
    }

    public void setDiffuse(float r, float g, float b) {
        mDiffuse[0] = r;
        mDiffuse[1] = g;
        mDiffuse[2] = b;
    }

    public void setAmbient(float r, float g, float b) {
        mAmbient[0] = r;
        mAmbient[1] = g;
        mAmbient[2] = b;
    }


    public void setSpecular(float r, float g, float b) {
        mSpecular[0] = r;
        mSpecular[1] = g;
        mSpecular[2] = b;
    }

    public void setSpecularShininess(float specularShininess) {
        this.mSpecularShininess = specularShininess;
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
    }

    public float[] getEmissive() {
        return mEmissive;
    }

    public float[] getAmbient() {
        return mAmbient;
    }

    public float[] getDiffuse() {
        return mDiffuse;
    }

    public float[] getSpecular() {
        return mSpecular;
    }

    public float getSpecularShininess() {
        return mSpecularShininess;
    }

    public float getAlpha() {
        return mAlpha;
    }
}
