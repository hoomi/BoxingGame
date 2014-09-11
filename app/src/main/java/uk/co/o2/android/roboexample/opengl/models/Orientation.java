package uk.co.o2.android.roboexample.opengl.models;

import android.content.Context;
import android.opengl.Matrix;

import org.jetbrains.annotations.NotNull;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class Orientation {

    private Vector3 mRight;
    private Vector3 mUp;
    private Vector3 mForward;

    private Vector3 mPosition;
    private float mRotationAngle;
    private Vector3 mRotationAxis;
    private Vector3 mScale;

    private float[] mOrientationMatrix = new float[16];
    private float[] mPositionMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private float[] mScaleMatrix = new float[16];

    private float[] tempMatrix = new float[16];
    private Vector3 mUpWorldVec = new Vector3(0, 0, 0);
    private Vector3 mRightWorldVec = new Vector3(0, 0, 0);
    private Vector3 mForwardWorldVec = new Vector3(0, 0, 0);

    public Orientation() {
        this.mRight = new Vector3(1.0f, 0.0f, 0.0f);
        this.mUp = new Vector3(0.0f, 1.0f, 0.0f);
        this.mForward = new Vector3(0.0f, 0.0f, 1.0f);


        this.mPosition = new Vector3(0.0f, 0.0f, 0.0f);
        this.mScale = new Vector3(1.0f, 1.0f, 1.0f);

        this.mRotationAngle = 0.0f;
        this.mRotationAxis = new Vector3(0.0f, 1.0f, 0.0f);

        Matrix.setIdentityM(mOrientationMatrix, 0);
        Matrix.setIdentityM(mRotationMatrix, 0);
    }


    public Vector3 getRight() {
        return mRight;
    }

    public void setRight(@NotNull Vector3 right) {
        this.mRight = right;
    }

    public Vector3 getUp() {
        return mUp;
    }

    public void setUp(Vector3 up) {
        this.mUp = up;
    }

    public Vector3 getForward() {
        return mForward;
    }

    public void setForward(@NotNull Vector3 forward) {
        this.mForward = forward;
    }

    public Vector3 getUpWorldCoordinates() {
        float[] upWorld = new float[4];
        float[] upLocal = new float[4];

        upLocal[0] = mUp.x;
        upLocal[1] = mUp.y;
        upLocal[2] = mUp.z;
        upLocal[3] = 1;

        Matrix.multiplyMV(upWorld, 0, mRotationMatrix, 0, upLocal, 0);
        mUpWorldVec.set(upWorld[0], upWorld[1], upWorld[2]);
        mUpWorldVec.normalize();
        return mUpWorldVec;
    }


    public Vector3 getRightWorldCoordinates() {
        float[] rightWorld = new float[4];
        float[] rightLocal = new float[4];

        rightLocal[0] = mRight.x;
        rightLocal[1] = mRight.y;
        rightLocal[2] = mRight.z;
        rightLocal[3] = 1;

        Matrix.multiplyMV(rightWorld, 0, mRotationMatrix, 0, rightLocal, 0);
        mRightWorldVec.set(rightWorld[0], rightWorld[1], rightWorld[2]);
        mRightWorldVec.normalize();
        return mRightWorldVec;

    }

    public Vector3 getForwardWorldCoordinates() {
        float[] forwardWorld = new float[4];
        float[] forwardLocal = new float[4];

        forwardLocal[0] = mForward.x;
        forwardLocal[1] = mForward.y;
        forwardLocal[2] = mForward.z;
        forwardLocal[3] = 1;

        Matrix.multiplyMV(forwardWorld, 0, mRotationMatrix, 0, forwardLocal, 0);
        mForwardWorldVec.set(forwardWorld[0], forwardWorld[1], forwardWorld[2]);
        mForwardWorldVec.normalize();
        return mForwardWorldVec;
    }


    public Vector3 getPosition() {
        return mPosition;
    }

    public void setPosition(@NotNull Vector3 position) {
        this.mPosition = position;
    }


    public float getRotationAngle() {
        return mRotationAngle;
    }

    public Vector3 getRotationAxis() {
        return mRotationAxis;
    }

    public void setRotationAxis(@NotNull Vector3 axis) {
        this.mRotationAxis = axis;
    }

    public Vector3 getScale() {
        return mScale;
    }

    public void setScale(@NotNull Vector3 scale) {
        this.mScale = scale;
    }

    public float[] getPositionMatrix() {
        return mPositionMatrix;
    }

    public float[] getRotationMatrix() {
        return mRotationMatrix;
    }

    public float[] getScaleMatrix() {
        return mScaleMatrix;
    }

    public void setPositionMatrix(@NotNull Vector3 position) {
        Matrix.setIdentityM(mPositionMatrix, 0);
        Matrix.translateM(mPositionMatrix, 0, position.x, position.y, position.z);
    }

    public void setRotationMatrix(float angle, @NotNull Vector3 axis) {
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.setRotateM(mRotationMatrix, 0, angle, axis.x, axis.y, axis.z);
    }

    public void setScaleMatrix(@NotNull Vector3 scale) {
        Matrix.setIdentityM(mScaleMatrix, 0);
        Matrix.scaleM(mScaleMatrix, 0, scale.x, scale.y, scale.z);
    }

    public void addRotation(float angleIncrementDegree) {
        mRotationAngle += angleIncrementDegree;
        Matrix.rotateM(mRotationMatrix, 0, angleIncrementDegree, mRotationAxis.x, mRotationAxis.y, mRotationAxis.z);
    }

    public float[] updateOrientation() {
        setPositionMatrix(mPosition);
        setScaleMatrix(mScale);
        Matrix.multiplyMM(tempMatrix, 0, mPositionMatrix, 0, mRotationMatrix, 0);

        Matrix.multiplyMM(mOrientationMatrix, 0, tempMatrix, 0, mScaleMatrix, 0);

        return mOrientationMatrix;
    }
}
