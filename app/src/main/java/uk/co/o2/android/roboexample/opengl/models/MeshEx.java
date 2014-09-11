package uk.co.o2.android.roboexample.opengl.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ShortBuffer;


public class MeshEx {
    // Use Indexed Drawing method
    private FloatBuffer mVertexBuffer = null; // Holds mesh vertex data
    private ShortBuffer mDrawListBuffer = null; // Holds index values into the vertex buffer that indicate
    // the order in which to draw the vertices.

    private int mCoordsPerVertex = 3;
    private static final int FLOAT_SIZE_BYTES = 4;
    private int m_VertexCount = 0;
    private int mMeshVerticesDataStrideBytes = mCoordsPerVertex * FLOAT_SIZE_BYTES;
    private int mMeshVerticesDataPosOffset = 0;
    private int mMeshVerticesDataUVOffset = -1;
    private int mMeshVerticesDataNormalOffset = -1;

    private boolean mMeshHasUV = false;
    private boolean mMeshHasNormals = false;


    public MeshEx(int coordsPerVertex,
                  int meshVerticesDataPosOffset,
                  int meshVerticesUVOffset,
                  int meshVerticesNormalOffset,
                  float[] vertices,
                  short[] drawOrder
    ) {
        mCoordsPerVertex = coordsPerVertex;
        mMeshVerticesDataStrideBytes = mCoordsPerVertex * FLOAT_SIZE_BYTES;
        mMeshVerticesDataPosOffset = meshVerticesDataPosOffset;
        mMeshVerticesDataUVOffset = meshVerticesUVOffset;
        mMeshVerticesDataNormalOffset = meshVerticesNormalOffset;

        if (mMeshVerticesDataUVOffset >= 0) {
            mMeshHasUV = true;
        }

        if (mMeshVerticesDataNormalOffset >= 0) {
            mMeshHasNormals = true;
        }

        // Allocate Vertex Buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                vertices.length * FLOAT_SIZE_BYTES);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();

        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        m_VertexCount = vertices.length / mCoordsPerVertex;

        // Initialize DrawList Buffer
        mDrawListBuffer = ShortBuffer.wrap(drawOrder);

    }

    void setUpMeshArrays(int posHandle, int texHandle, int normalHandle) {
        //glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int offset)
        //glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr)

        // Set up stream to position variable in shader
        mVertexBuffer.position(mMeshVerticesDataPosOffset);
        GLES20.glVertexAttribPointer(posHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                mMeshVerticesDataStrideBytes,
                mVertexBuffer);

        GLES20.glEnableVertexAttribArray(posHandle);


        if (mMeshHasUV) {
            // Set up Vertex Texture Data stream to shader
            mVertexBuffer.position(mMeshVerticesDataUVOffset);
            GLES20.glVertexAttribPointer(texHandle,
                    2,
                    GLES20.GL_FLOAT,
                    false,
                    mMeshVerticesDataStrideBytes,
                    mVertexBuffer);
            GLES20.glEnableVertexAttribArray(texHandle);
        }

        if (mMeshHasNormals) {

            // Set up Vertex Texture Data stream to shader
            mVertexBuffer.position(mMeshVerticesDataNormalOffset);
            GLES20.glVertexAttribPointer(normalHandle,
                    3,
                    GLES20.GL_FLOAT,
                    false,
                    mMeshVerticesDataStrideBytes,
                    mVertexBuffer);
            GLES20.glEnableVertexAttribArray(normalHandle);
        }
    }

    public static void checkGLError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("ERROR IN MESHEX", glOperation + " IN CHECKGLERROR() : glError - " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    void drawMesh(int posHandle, int texHandle, int normalHandle) {
        setUpMeshArrays(posHandle, texHandle, normalHandle);


        //glDrawElements (int mode, int count, int type, int offset)
        //glDrawElements (int mode, int count, int type, Buffer indices)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                mDrawListBuffer.capacity(),
                GLES20.GL_UNSIGNED_SHORT,
                mDrawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(posHandle);
        checkGLError("glDisableVertexAttribArray ERROR - posHandle");

        if (mMeshHasUV) {
            GLES20.glDisableVertexAttribArray(texHandle);
            checkGLError("glDisableVertexAttribArray ERROR - TexHandle");
        }
        if (mMeshHasNormals) {
            GLES20.glDisableVertexAttribArray(normalHandle);
            checkGLError("glDisableVertexAttribArray ERROR - normalHandle");
        }
    }

}
