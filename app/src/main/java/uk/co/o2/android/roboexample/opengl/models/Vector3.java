package uk.co.o2.android.roboexample.opengl.models;

import android.util.FloatMath;

import junit.framework.Assert;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class Vector3 {
    public volatile float x;
    public volatile float y;
    public volatile float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Scalar multiplication
     *
     * @param v multiplier
     */
    public void multiply(float v) {
        x *= v;
        y *= v;
        z *= v;
    }


    /**
     * It adds two vector together
     *
     * @param left  first vector (if null second vector is returned)
     * @param right second vector ( if null first vector will be returned)
     * @return return the sum of the two vectors
     */
    public static Vector3 add(Vector3 left, Vector3 right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        return new Vector3(left.x + right.x, left.y + right.y, left.z + right.z);
    }


    /**
     * Return the cross product of the two vector (Remember the order is important!!!!)
     * @param left first vector. It cannot be null
     * @param right second vector. It cannot be null
     * @return the cross product of the two vectors 
     */
    public static Vector3 crossProduct(Vector3 left, Vector3 right) {
        assert (left != null);
        assert (right != null);
        return new Vector3(left.y * right.z - left.z * right.y,
                left.z * right.x - left.x * right.z,
                left.x * right.y - left.y * right.x);

    }

    /**
     * It sets the new values of x,y and z
     *
     * @param x new x value
     * @param y new y value
     * @param z new z value
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * It normalizes the vector
     */
    public void normalize() {
        float length = length();
        x /= length;
        y /= length;
        z /= length;
    }

    /**
     * It calculates the length of the vector
     *
     * @return it returns the length of the vector
     */
    public float length() {
        return FloatMath.sqrt(x * x + y * y + z * z);
    }

}
