package uk.co.o2.android.roboexample.opengl.models;

import android.content.Context;
import android.opengl.GLES20;
import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class Shader {

    private final static String TAG = "Shader";
    private int mFragmentShader;
    private int mVertexShader;
    private int mShaderProgram;

    public int getFragmentShader() {
        return mFragmentShader;
    }

    public int getVertexShader() {
        return mVertexShader;
    }

    public int getShaderProgram() {
        return mShaderProgram;
    }

    String readInShader(Context context, int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(resourceId);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String read;
        try {
            while ((read = bufferedReader.readLine()) != null) {
                stringBuilder.append(read).append("\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initFragmentShader(Context context, int resourceId) {
        String fShaderString = readInShader(context, resourceId);
        mFragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(mFragmentShader, fShaderString);
        GLES20.glCompileShader(mFragmentShader);

        IntBuffer compileErrorStatus = IntBuffer.allocate(1);
        GLES20.glGetShaderiv(mFragmentShader, GLES20.GL_COMPILE_STATUS, compileErrorStatus);
        if (compileErrorStatus.get(0) == 0) {
            Log.e(TAG, "Could not compile Fragment shader file =  " + String.valueOf(resourceId));
            Log.e(TAG, GLES20.glGetShaderInfoLog(mFragmentShader));
            GLES20.glDeleteShader(mFragmentShader);
            mFragmentShader = 0;
        } else {
            GLES20.glAttachShader(mShaderProgram, mFragmentShader);
            Log.d(TAG, "In InitFragmentShader()");
        }
    }

    private void initVertexShader(Context context, int resourceId) {
        String vShaderString = readInShader(context, resourceId);
        mVertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(mVertexShader, vShaderString);
        GLES20.glCompileShader(mVertexShader);

        IntBuffer compileErrorStatus = IntBuffer.allocate(1);
        GLES20.glGetShaderiv(mVertexShader, GLES20.GL_COMPILE_STATUS, compileErrorStatus);
        if (compileErrorStatus.get(0) == 0) {
            Log.e(TAG, "Could not compile vertex shader file =  " + String.valueOf(resourceId));
            Log.e(TAG, GLES20.glGetShaderInfoLog(mVertexShader));
            GLES20.glDeleteShader(mVertexShader);
            mVertexShader = 0;
        } else {
            GLES20.glAttachShader(mShaderProgram, mVertexShader);
            Log.d(TAG, "In InitFragmentShader()");
        }

    }

    private void initShaderProgram(Context context, int vResourceId, int fResourceId) {
        mShaderProgram = GLES20.glCreateProgram();
        initVertexShader(context, vResourceId);
        initFragmentShader(context, fResourceId);
        GLES20.glLinkProgram(mShaderProgram);

        String debugInfo = GLES20.glGetProgramInfoLog(mShaderProgram);
        Log.d(TAG, debugInfo);
    }

    public Shader(Context context, int vResourceId, int fResourceId) {
        mShaderProgram = 0;
        mFragmentShader = 0;
        mVertexShader = 0;

        initShaderProgram(context, vResourceId, fResourceId);
    }

    public int getShaderVertexAttributeVariableLocation(String variableName) {

        return GLES20.glGetAttribLocation(mShaderProgram, variableName);
    }

    public int getShaderUniformVariableLocation(String variableName) {
        return GLES20.glGetUniformLocation(mShaderProgram, variableName);
    }

    public void setShaderUniformVariableValue(String variableName, float... values) {
        int loc = GLES20.glGetUniformLocation(mShaderProgram, variableName);
        if (values == null || values.length == 0) {
            return;
        }
        int length = values.length;
        switch (length) {
            case 1:
                GLES20.glUniform1f(loc, values[0]);
                break;
            case 2:
                GLES20.glUniform2f(loc, values[0], values[1]);
                break;
            case 3:
                GLES20.glUniform3f(loc, values[0], values[1], values[2]);
                break;
            default:
                GLES20.glUniform4f(loc, values[0], values[1], values[2], values[3]);
                break;
        }
    }

    public void setShaderUniformVariableValue(String variableName, @NotNull Vector3 values) {
        int loc = GLES20.glGetUniformLocation(mShaderProgram, variableName);
        GLES20.glUniform3f(loc, values.x, values.y, values.z);
    }

    public void setShaderVariableValueFloat1Array(String arrayName, int count,
                                                  float[] matrix, int offset) {
        int loc = GLES20.glGetUniformLocation(mShaderProgram, arrayName);
        GLES20.glUniform1fv(loc, count, matrix, offset);

    }

    public void setShaderVariableValueFloat3Array(String arrayName, int count,
                                                  float[] matrix, int offset) {
        int loc = GLES20.glGetUniformLocation(mShaderProgram, arrayName);
        GLES20.glUniform3fv(loc, count, matrix, offset);

    }

    public void setShaderVariableValueFloatMatrix3Array(String matrixName, int count,
                                                        boolean transpose, float[] matrix, int offset) {
        int loc = GLES20.glGetUniformLocation(mShaderProgram, matrixName);
        GLES20.glUniformMatrix3fv(loc, count, transpose, matrix, offset);
    }

    public void setShaderVariableValueFloatMatrix4Array(String matrixName, int count,
                                                        boolean transpose, float[] matrix, int offset) {
        int loc = GLES20.glGetUniformLocation(mShaderProgram, matrixName);
        GLES20.glUniformMatrix4fv(loc, count, transpose, matrix, offset);
    }

    public void activateShader() {
        GLES20.glUseProgram(mShaderProgram);
    }

    public void deactivateShader() {
        GLES20.glUseProgram(0);
    }
}
