package uk.co.o2.android.roboexample.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import uk.co.o2.android.roboexample.R;
import uk.co.o2.android.roboexample.opengl.models.Camera;
import uk.co.o2.android.roboexample.opengl.models.CollisionStatus;
import uk.co.o2.android.roboexample.opengl.models.Cube;
import uk.co.o2.android.roboexample.opengl.models.Material;
import uk.co.o2.android.roboexample.opengl.models.MeshEx;
import uk.co.o2.android.roboexample.opengl.models.Orientation;
import uk.co.o2.android.roboexample.opengl.models.Physics;
import uk.co.o2.android.roboexample.opengl.models.PointLight;
import uk.co.o2.android.roboexample.opengl.models.Shader;
import uk.co.o2.android.roboexample.opengl.models.Texture;
import uk.co.o2.android.roboexample.opengl.models.Vector3;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    private PointLight mPointLight;
    private Camera mCamera;

    private int mViewPortWidth;
    private int mViewPortHeight;

    private Vector3 mCubePositionDelta = new Vector3(0.02f, 0.0f, 0.0f);
    private Vector3 mCubeScale = new Vector3(1.0f, 1.0f, 0.2f);
    private Vector3 mCubeRotation = new Vector3(0.0f, 1.0f, 0.0f);
    private Vector3 mForce = new Vector3(0,20,0);
    private float mRotationalForce = 3;

    private Cube mCube;
    private Cube mCube2;

    public MyGLRenderer(Context context) {
        this.mContext = context;
    }

    private void setupLights() {
        Vector3 lightPosition = new Vector3(0, 125, 125);

        float[] ambientColor = new float[]{0.0f, 0.0f, 0.0f};
        float[] diffuseColor = new float[]{1.0f, 1.0f, 1.0f};
        float[] specularColor = new float[]{1.0f, 1.0f, 1.0f};

        mPointLight.setPosition(lightPosition);
        mPointLight.setAmbientColor(ambientColor);
        mPointLight.setDiffuseColor(diffuseColor);
        mPointLight.setSpecularColor(specularColor);
    }

    private void setUpCamera() {
        Vector3 eye = new Vector3(0, 0, 20);
        Vector3 centre = new Vector3(0, 0, -1);
        Vector3 up = new Vector3(0, 1, 0);

        float ratio = (float) mViewPortWidth / mViewPortHeight;
        float projLeft = -ratio;
        float projRight = ratio;
        float projTop = 1.0f;
        float projBottom = -1.0f;
        float projNear = 3;
        float projFar = 50;
        mCamera = new Camera(eye, centre, up,
                projLeft, projRight, projTop,
                projBottom, projNear, projFar);
    }

    private void createCube(Context context) {
        Shader shader = new Shader(context, R.raw.vsonlight, R.raw.fsonlight);
//        MeshEx cubeMesh = new MeshEx(8, 0, 3, 5, Cube.cubeData, Cube.cubeDrawOrder);
        MeshEx cubeMesh = new MeshEx(8, 0, 3, 5, Cube.CubeData4Sided, Cube.cubeDrawOrder);
        Material material = new Material();
//        material.setEmissive(0.0f, 0, 0.25f);

        Texture textureAndroid = new Texture(context, R.drawable.ic_launcher);

        Texture[] cubeTex = new Texture[]{textureAndroid};
        mCube = new Cube(cubeMesh, cubeTex, material, shader);
        mCube.getObjectPhysics().setGravity(true);

        Vector3 axis = new Vector3(0, 1, 0);
        Vector3 position = new Vector3(0, 10, 0);
        Vector3 scale = new Vector3(1, 1, 1);

        Orientation orientation = mCube.getOrientation();
        orientation.setPosition(position);
        orientation.setRotationAxis(axis);
        orientation.setScale(scale);
    }

    private void createCube2(Context context) {
        Shader shader = new Shader(context, R.raw.vsonlight, R.raw.fsonlight);
//        MeshEx cubeMesh = new MeshEx(8, 0, 3, 5, Cube.cubeData, Cube.cubeDrawOrder);
        MeshEx cubeMesh = new MeshEx(8, 0, 3, 5, Cube.CubeData4Sided, Cube.cubeDrawOrder);
        Material material = new Material();
//        material.setEmissive(0.0f, 0, 0.25f);

        Texture textureAndroid = new Texture(context, R.drawable.test);

        Texture[] cubeTex = new Texture[]{textureAndroid};
        mCube2 = new Cube(cubeMesh, cubeTex, material, shader);
        mCube2.getObjectPhysics().setGravity(true);

        Vector3 axis = new Vector3(0, 1, 0);
        Vector3 position = new Vector3(0, 4 , 0);
        Vector3 scale = new Vector3(1, 1, 1);

        Orientation orientation = mCube2.getOrientation();
        orientation.setPosition(position);
        orientation.setRotationAxis(axis);
        orientation.setScale(scale);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mPointLight = new PointLight();
        setupLights();
        createCube(mContext);
        createCube2(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mViewPortHeight = height;
        mViewPortWidth = width;

        setUpCamera();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        mCamera.updateCamera();
        mCube.updateObject3d();

        Physics physics = mCube.getObjectPhysics();
        if (physics.getHitGroundStatus()) {
            physics.applyTranslationalForce(mForce);
            physics.applyRotationalForce(mRotationalForce, 10.0f);
        }


        mCube2.updateObject3d();

        CollisionStatus typeCollision = mCube.getObjectPhysics().checkForCollisionSphereBounding(mCube, mCube2);

        if ((typeCollision == CollisionStatus.COLLISION) ||
                (typeCollision == CollisionStatus.PENETRATING_COLLISION))
        {
            mCube.getObjectPhysics().applyLinearImpulse(mCube, mCube2);
        }


//        // Rotation of the cube
//        mCube.getOrientation().addRotation(1);
//
//        // Moves the cube forward and backwards
//        Vector3 currentPosition = mCube.getOrientation().getPosition();
//        if (currentPosition.x < -1|| currentPosition.x > 1) {
//            mCubePositionDelta.negate();
//        }
//        Vector3 newPosition = Vector3.add(currentPosition, mCubePositionDelta);
//        currentPosition.set(newPosition.x, newPosition.y, newPosition.z);
//
//        mCube.getOrientation().setScale(mCubeScale);

        mCube.drawObject(mCamera, mPointLight);
        mCube2.drawObject(mCamera, mPointLight);

    }
}
