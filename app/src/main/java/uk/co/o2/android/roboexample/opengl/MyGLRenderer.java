package uk.co.o2.android.roboexample.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import uk.co.o2.android.roboexample.R;
import uk.co.o2.android.roboexample.opengl.models.Camera;
import uk.co.o2.android.roboexample.opengl.models.Cube;
import uk.co.o2.android.roboexample.opengl.models.Material;
import uk.co.o2.android.roboexample.opengl.models.MeshEx;
import uk.co.o2.android.roboexample.opengl.models.Orientation;
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

    private Cube mCube;

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
        Vector3 eye = new Vector3(0, 0, 8);
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
        Shader shader = new Shader(context, R.raw.vsonlight,R.raw.fsonlight);
        MeshEx cubeMesh = new MeshEx(8,0,3,5,Cube.cubeData,Cube.cubeDrawOrder);
        Material material = new Material();
//        material.setEmissive(0.0f, 0, 0.25f);

        Texture textureAndroid = new Texture(context,R.drawable.ic_launcher);

        Texture[] cubeTex = new Texture[]{textureAndroid};
        mCube = new Cube(cubeMesh,cubeTex,material,shader);

        Vector3 axis = new Vector3(0,1,0);
        Vector3 position = new Vector3(0,0,0);
        Vector3 scale = new Vector3(1,1,1);

        Orientation orientation = mCube.getOrientation();
        orientation.setPosition(position);
        orientation.setRotationAxis(axis);
        orientation.setScale(scale);
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mPointLight = new PointLight();
        setupLights();
        createCube(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        mViewPortHeight = height;
        mViewPortWidth = width;

        setUpCamera();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        mCamera.updateCamera();

        mCube.getOrientation().addRotation(1);
        mCube.drawObject(mCamera,mPointLight);
    }
}
