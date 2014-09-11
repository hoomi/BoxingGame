package uk.co.o2.android.roboexample.opengl.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture {
    private final static String TAG = Texture.class.getSimpleName();
    private int mTextureId;
    private Bitmap mBitmap;

    public Texture(Context context, int resourceId) {

        //Create new texture resource from resourceId
        initTexture(context, resourceId);

        // Setup default texture parameters
        setTextureMIN_FLITER(GLES20.GL_NEAREST);
        setTextureMAG_FILTER(GLES20.GL_LINEAR);
        setTextureWRAP_S(GLES20.GL_CLAMP_TO_EDGE);
        setTextureWRAP_T(GLES20.GL_CLAMP_TO_EDGE);

    }

    private void initTexture(Context context, int resourceId) {

        /*
         * public static void glGenTextures (int n, int[] textures, int offset)
		 * Returns n currently unused names for texture objects in the array textures.
		 */
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        checkGLError("glGenTextures");
        /*
         * public static void glBindTexture (int target, int texture)
		 */
        mTextureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        checkGLError("glBindTexture");

        // Load texture from resources
        loadTexture(context, resourceId);


        /*
		 * static void	 texImage2D(int target, int level, Bitmap bitmap, int border)
		 * A version of texImage2D that determines the internalFormat and type automatically.
		 */
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        checkGLError("texImage2D");

    }

    private void loadTexture(Context context, int resourceId) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    public static void setActiveUnit(int unitNumber) {
        GLES20.glActiveTexture(unitNumber);

    }

    public void checkGLError(String glOperation) {
        int error;
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("class Texture :", glOperation + " IN CHECKGLERROR() : glError " + GLU.gluErrorString(error));
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public Bitmap getTextureBitmap() {
        return mBitmap;
    }

    private void setTextureWRAP_S(int value) {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, value);
    }


    private void setTextureWRAP_T(int value) {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, value);
    }

    private void setTextureMAG_FILTER(int value) {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, value);
    }

    private void setTextureMIN_FLITER(int value) {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, value);
    }


    public void activateTexture() {
        if (mTextureId != 0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        } else {
            Log.e(TAG, "ERROR - Texture ERROR- m_TextureId = 0 Error in ActivateTexture()! ");
        }
    }

    public void setTextureEnvMODE(float value) {
        GLES10.glTexEnvf(GLES10.GL_TEXTURE_ENV, GLES10.GL_TEXTURE_ENV_MODE, value);
    }
}
