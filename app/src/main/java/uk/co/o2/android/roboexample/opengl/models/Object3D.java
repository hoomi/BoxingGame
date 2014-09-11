package uk.co.o2.android.roboexample.opengl.models;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class Object3D {
    private final static String TAG = Object3D.class.getSimpleName();
    Orientation mOrientation = null;

    private MeshEx mMeshEx = null;
    private Texture[] mTextures = null;
    private Material mMaterial = null;
    private Shader mShader = null;

    private float[] mMVPMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mModelViewMatrix = new float[16];
    private float[] mNormalMatrix = new float[16];
    private float[] mNormalMatrixInvert = new float[16];

    private int mPositionHandle;
    private int mTextureHandler;
    private int mNormalHandle;

    private int mActiveTexture = -1;

    //Texture animation control
    private boolean mAnimateTextures = false;
    private int mStartTexAnimNum = 0;
    private int mStopTexAnimNum = 0;
    private float mTextAnimDelay = 0;
    private float mTargetTime = 0;
    private float mCounter = 0;

    public Object3D(MeshEx meshEx, Texture[] textures, Material material, Shader shader) {
        this.mMeshEx = meshEx;
        this.mTextures = textures;
        this.mMaterial = material;
        this.mShader = shader;
        if (mTextures == null) {
            mActiveTexture = -1;
        } else {
            mActiveTexture = 0;
        }
        mOrientation = new Orientation();
    }

    public static void checkGLError(String glOperation) {
        int error;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + " IN CHECKGLERROR() : glError " + GLU.gluErrorString(error));
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public boolean setTexture(int textureNumber) {
        if (textureNumber < mTextures.length) {
            mActiveTexture = textureNumber;
            return true;
        }
        return false;
    }

    public Texture getTexture(int textureNumber) {
        if (textureNumber < mTextures.length) {
            return mTextures[textureNumber];
        }
        return null;
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public void activateTexture() {
        if (mActiveTexture >= 0) {
            Texture.setActiveUnit(GLES20.GL_TEXTURE0);
            checkGLError("glActivateTexture - setActiveTexture unit failed");

            if (mAnimateTextures && mCounter >= mTargetTime) {
                mActiveTexture++;
                if (mActiveTexture > mStopTexAnimNum) {
                    mActiveTexture = mStartTexAnimNum;
                }
                mTargetTime = mCounter + mTextAnimDelay;
            }
            mCounter = SystemClock.uptimeMillis() / 1000.0f;

            //Activate texture for this object
            mTextures[mActiveTexture].activateTexture();
        }
    }

    private void getVertexAttribInfo() {
        mPositionHandle = mShader.getShaderVertexAttributeVariableLocation("aPosition");
        checkGLError("glGetAttribLocation - aPosition");

        mTextureHandler = mShader.getShaderVertexAttributeVariableLocation("aTextureCoord");
        checkGLError("glGetAttribLocation - aTextureCoord");

        mNormalHandle = mShader.getShaderVertexAttributeVariableLocation("aNormal");
        checkGLError("glGEtAttribLocation - aNormal");
    }


    public void setLighting(Camera camera, PointLight pointLight,
                            float[] modelMatrix, float[] viewMatrix,
                            float[] modelViewMatrix, float[] normalMatrix) {
        float[] ambientColor = pointLight.getAmbientColor();
        float[] diffuseColor = pointLight.getDiffuseColor();
        float[] specularColor = pointLight.getSpecularColor();
        float specularShininess = pointLight.getSpecularShininess();

        Vector3 eyePos = camera.getEye();
        mShader.setShaderUniformVariableValue("uLightAmbient", ambientColor);
        mShader.setShaderUniformVariableValue("uLightDiffuse", diffuseColor);
        mShader.setShaderUniformVariableValue("uLightSpecular", specularColor);
        mShader.setShaderUniformVariableValue("uLightShininess", specularShininess);
        mShader.setShaderUniformVariableValue("uWorldLightPos", pointLight.getPosition());

        mShader.setShaderUniformVariableValue("uEyePosition", eyePos);

        mShader.setShaderVariableValueFloatMatrix4Array("NormalMatrix", 1, false, normalMatrix,0);
        mShader.setShaderVariableValueFloatMatrix4Array("uModelMatrix", 1, false, modelMatrix,0);
        mShader.setShaderVariableValueFloatMatrix4Array("uViewMatrix", 1, false, viewMatrix,0);
        mShader.setShaderVariableValueFloatMatrix4Array("uModelViewMatrix", 1, false, modelViewMatrix,0);

        if (mMaterial != null) {
            mShader.setShaderUniformVariableValue("uMatEmissive", mMaterial.getEmissive());
            mShader.setShaderUniformVariableValue("uMatAmbient", mMaterial.getAmbient());
            mShader.setShaderUniformVariableValue("uMatDiffuse", mMaterial.getEmissive());
            mShader.setShaderUniformVariableValue("uMatSpecular", mMaterial.getSpecular());
            mShader.setShaderUniformVariableValue("uMatAlpha", mMaterial.getAlpha());
        }
    }

    private void generateMatrices(Camera camera,
                                  Vector3 position,
                                  Vector3 rotationAxis,
                                  Vector3 scale) {
        // Initialize model matrix
        Matrix.setIdentityM(mModelMatrix, 0);

        mOrientation.setPosition(position);
        mOrientation.setRotationAxis(rotationAxis);
        mOrientation.setScale(scale);

        mModelMatrix = mOrientation.updateOrientation();

        // Create model view matrix
        Matrix.multiplyMM(mModelViewMatrix, 0, camera.getViewMatrix(), 0, mModelMatrix, 0);

        // Create normal matrix for lighting
        Matrix.multiplyMM(mNormalMatrix, 0, camera.getViewMatrix(), 0, mModelMatrix, 0);
        Matrix.invertM(mNormalMatrixInvert, 0, mNormalMatrix, 0);
        Matrix.transposeM(mNormalMatrix, 0, mNormalMatrixInvert, 0);

        // Create model view projection matrix
        Matrix.multiplyMM(mMVPMatrix, 0, camera.getProjectionMatrix(), 0, mModelViewMatrix, 0);
    }

    public void drawObject(Camera camera,
                           PointLight pointLight,
                           Vector3 position,
                           Vector3 rotationAxis,
                           Vector3 scale) {

        //Activate and setup the shade and draw object's mesh

        // Generate needed matrices for object
        generateMatrices(camera, position, rotationAxis, scale);

        // Add program to OpenGL environment
        mShader.activateShader();

        // Get vertex attribute info in preparation for drawing the mesh
        getVertexAttribInfo();

        // Setup the lighting parameters for this object
        setLighting(camera, pointLight, mModelMatrix, camera.getViewMatrix(), mModelViewMatrix, mNormalMatrix);

        // Apply the projection and view transformation matrix to the shader
        mShader.setShaderVariableValueFloatMatrix4Array("uMVPMatrix", 1, false, mMVPMatrix, 0);

        // Activate texture for this object
        activateTexture();

        // Enable hidden surface removal
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        if (mMeshEx != null) {
            mMeshEx.drawMesh(mPositionHandle, mTextureHandler, mNormalHandle);
        } else {
            Log.d(TAG, " No mesh in Object3D");
        }
    }

    public void drawObject(Camera camera, PointLight pointLight) {
        drawObject(camera, pointLight, mOrientation.getPosition(), mOrientation.getRotationAxis(), mOrientation.getScale());
    }
}



