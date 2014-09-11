package uk.co.o2.android.roboexample.opengl.models;

import android.content.Context;
import android.opengl.Matrix;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class Camera {
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    //Viewing frustrum
    private float mProjLeft = 0;
    private float mProjRight = 0;
    private float mProjBottom = 0;
    private float mProjTop = 0;
    private float mProjNear = 0;
    private float mProjFar = 0;


    //Camera location and orientation
    private Vector3 mEye = new Vector3(0, 0, 0);
    private Vector3 mCentre = new Vector3(0, 0, 0);
    private Vector3 mUp = new Vector3(0, 0, 0);

    private Orientation mOrientation = null;

    public Camera(Vector3 eye, Vector3 centre, Vector3 up,
                  float projLeft, float projRight,
                  float projTop, float projBottom,
                  float projNear, float projFar) {
        setCameraProjection(projLeft, projRight, projTop, projBottom, projNear, projFar);

        mOrientation = new Orientation();
        mOrientation.getForward().set(centre.x,centre.y,centre.z);
        mOrientation.getUp().set(up.x,up.y,up.z);
        mOrientation.getPosition().set(eye.x,eye.y,eye.z);

        Vector3 cameraRight = Vector3.crossProduct(centre,up);
        cameraRight.normalize();
        mOrientation.setRight(cameraRight);
    }

    public void setCameraProjection(float projLeft, float projRight,
                                    float projTop, float projBottom,
                                    float projNear, float projFar) {
        this.mProjLeft = projLeft;
        this.mProjRight = projRight;
        this.mProjTop = projTop;
        this.mProjBottom = projBottom;
        this.mProjNear = projNear;
        this.mProjFar = projFar;
        //Defines a projection matrix in terms of six clip planes.
        Matrix.frustumM(mProjectionMatrix, 0, mProjLeft, mProjRight, mProjBottom, mProjTop, mProjNear, mProjFar);
    }

    /**
     * It set the eye, centre and up vector while calculating the viewMatrix
     * @param eye Camera's eye vector
     * @param centre Camera's centre vector
     * @param up Camera's up vector
     */
    private void setCameraView(Vector3 eye, Vector3 centre, Vector3 up) {
        Matrix.setLookAtM(mViewMatrix, 0, eye.x, eye.y, eye.z,
                centre.x, centre.y, centre.z,
                up.x, up.y, up.z);
    }

    public Vector3 getEye() {
        return mEye;
    }

    public Vector3 getLookAtCentre() {
        return mCentre;
    }

    public Vector3 getUp() {
        return mUp;
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public float getProjLeft() {
        return mProjLeft;
    }

    public float getProjRight() {
        return mProjRight;
    }

    public float getProjBottom() {
        return mProjBottom;
    }

    public float getProjTop() {
        return mProjTop;
    }

    public float getProjNear() {
        return mProjNear;
    }

    public float getProjFar() {
        return mProjFar;
    }

    /**
     * @return Camera's view port width
     */
    public float getCameraViewPortWidth() {
        return Math.abs(mProjLeft - mProjRight);
    }

    /**
     * @return Camera's view port height
     */
    public float getCameraViewPortHeight() {
        return Math.abs(mProjTop - mProjBottom);
    }

    /**
     * @return Camera's view port depth
     */
    public float getCameraViewPortDepth() {
        return Math.abs(mProjNear - mProjFar);
    }

    /**
     * @return projection matrix
     */
    public float[] getProjectionMatrix() {
        return mProjectionMatrix;
    }

    /**
     * @return view matrix
     */
    public float[] getViewMatrix() {
        return mViewMatrix;
    }

    /**
     * Calculates the look at vector
     */
    public void calculateLookAtVector() {
        // lookAtVector = mProjFar * ForwardCameraUnitVecWorldCords
        Vector3 forwardsCoords = mOrientation.getForwardWorldCoordinates();
        mCentre.set(forwardsCoords.x, forwardsCoords.y, forwardsCoords.z);

        mCentre.multiply(mProjFar);
//        mCentre.multiply(5);
        mCentre = Vector3.add( mOrientation.getPosition(),mCentre);
    }

    /**
     * Calculates the eye position based on the orientation
     */
    public void calculatePosition() {
        Vector3 position = mOrientation.getPosition();
        mEye.set(position.x, position.y, position.z);
    }

    /**
     * Calculates up position base on the orientation
     */
    public void calculateUpVector() {
        Vector3 upCoords = mOrientation.getUpWorldCoordinates();
        mUp.set(upCoords.x, upCoords.y, upCoords.z);
    }

    public void updateCamera() {
        calculateLookAtVector();
        calculateUpVector();
        calculatePosition();
        setCameraView(mEye,mCentre,mUp);
    }
}
