package uk.co.o2.android.roboexample.opengl.models;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

/**
 * Created by hostova1 on 12/09/2014.
 */
public class Physics {

    static final float PI = 3.14159265358979323846264338327950288419716939937511f;
    static final float TWO_PI = 2 * PI;
    static final float HALF_PI = 0.5f * PI;
    static final float QUARTER_PI = 0.25f * PI;
    private static final float COLLISION_TOLERANCE = 0.1f;

    private float mCollisionTolerance = COLLISION_TOLERANCE;
    private float mCoefficientOfRestitution = 0.5f;

    private Vector3 mCollisionNormal;
    private Vector3 mRelativeVelocity;
    private Vector3 mLinearVelocity = new Vector3(0, 0, 0);
    private Vector3 mLinearAcceleration = new Vector3(0, 0, 0);
    private Vector3 mMaxLinearVelocity = new Vector3(1.25f, 1.25f, 1.25f);
    private Vector3 mMaxLinearAcceleration = new Vector3(1, 1, 1);

    private float mAngularVelocity = 0;
    private float mAngularAcceleration = 0;
    private float mMaxAngularAcceleration = HALF_PI;
    private float mMaxAngularVelocity = 4 * PI;

    private boolean mApplyGravity = false;
    private float mGravity = 0.01f;
    private float mGroundLevel = -2;
    private boolean mJustHitGround = false;
    private float mMass = 100.0f;


    public void applyTranslationalForce(@NotNull Vector3 force) {
        Vector3 a = force;
        if (mMass != 0) {
            a = Vector3.divide(force, mMass);
        }
        mLinearAcceleration.add(a);
    }


    public void applyRotationalForce(float force, float r) {
        // 1. Torque = r X F;
        //    T = I * AngularAcceleration;
        //    T/I = AngularAccleration;
        //
        //    I = mr^2 = approximate with hoop inertia with r = 1 so that I = mass;
        float torque = r * force;
        float angluarAcceleration = 0;
        float i = mMass;

        if (i != 0) {
            angluarAcceleration = torque / i;
        }
        mAngularAcceleration += angluarAcceleration;
    }

    private float updateValuesWithinLimit(float value, float increment, float limit) {
        float returnValue = 0;

        returnValue = value + increment;

        if (returnValue > limit) {
            returnValue = limit;
        } else if (returnValue < -limit) {
            returnValue = -limit;
        }
        return returnValue;
    }

    private float testSetLimitValues(float value, float limit) {
        float returnValue = value;
        if (returnValue < -limit) {
            returnValue = -limit;
        } else if (returnValue > limit) {
            returnValue = limit;
        }
        return returnValue;
    }

    private void applyGravityToObject() {
        mLinearAcceleration.y -= mGravity;
    }

    public void updatePhysicsObject(Orientation orientation) {
        if (mApplyGravity) {
            applyGravityToObject();
        }

        mLinearAcceleration.x = testSetLimitValues(mLinearAcceleration.x, mMaxLinearAcceleration.x);
        mLinearAcceleration.y = testSetLimitValues(mLinearAcceleration.y, mMaxLinearAcceleration.y);
        mLinearAcceleration.z = testSetLimitValues(mLinearAcceleration.z, mMaxLinearAcceleration.z);

        mLinearVelocity.add(mLinearAcceleration);
        mLinearVelocity.x = testSetLimitValues(mLinearVelocity.x, mMaxLinearVelocity.x);
        mLinearVelocity.y = testSetLimitValues(mLinearVelocity.y, mMaxLinearVelocity.y);
        mLinearVelocity.z = testSetLimitValues(mLinearVelocity.z, mMaxLinearVelocity.z);

        mAngularAcceleration = testSetLimitValues(mAngularAcceleration, mMaxAngularAcceleration);
        mAngularVelocity += mAngularAcceleration;
        mAngularAcceleration = testSetLimitValues(mAngularAcceleration, mMaxAngularAcceleration);

        mAngularAcceleration = 0;
        mLinearAcceleration.clear();

        Vector3 pos = orientation.getPosition();
        pos.add(mLinearVelocity);
        if (mApplyGravity) {
            if (pos.y < mGroundLevel && mLinearVelocity.y < 0) {
                if (Math.abs(mLinearVelocity.y) > Math.abs(mGravity)) {
                    mJustHitGround = true;
                }
                pos.y = mGroundLevel;
                mLinearVelocity.y = 0;
            } else {
                mJustHitGround = false;
            }
        } else {
            mJustHitGround = false;
        }

        orientation.addRotation(mAngularVelocity);
    }

    public void setGravity(boolean gravity) {
        mApplyGravity = gravity;
    }

    public boolean getHitGroundStatus() {
        return mJustHitGround;
    }

    public CollisionStatus checkForCollisionSphereBounding(Object3D object1, Object3D object2) {
        float impactRadiusSum = 0;
        float relativeVelocityNormal = 0;
        float collisionDistance = 0;
        Vector3 body1Velocity;
        Vector3 body2Velocity;

        impactRadiusSum = object1.getScaledRadius() + object2.getScaledRadius();
        Vector3 position1 = object1.getOrientation().getPosition();
        Vector3 position2 = object2.getOrientation().getPosition();
        Vector3 distanceVector = Vector3.subtract(position1, position2);
        collisionDistance = distanceVector.length() - impactRadiusSum;

        distanceVector.normalize();
        mCollisionNormal = distanceVector;

        body1Velocity = object1.getObjectPhysics().getVelocity();
        body2Velocity = object2.getObjectPhysics().getVelocity();

        mRelativeVelocity = Vector3.subtract(body1Velocity, body2Velocity);
        relativeVelocityNormal = mRelativeVelocity.dotProduct(mCollisionNormal);
        if (Math.abs(collisionDistance) <= mCollisionTolerance && relativeVelocityNormal < 0.0) {
            return CollisionStatus.COLLISION;
        }
        if (collisionDistance < -mCollisionTolerance && relativeVelocityNormal < 0.0f) {
            return CollisionStatus.PENETRATING_COLLISION;
        }
        if (collisionDistance < -mCollisionTolerance) {
            return CollisionStatus.PENETRATING;
        }
        return CollisionStatus.NO_COLLISION;
    }

    public void applyLinearImpulse(Object3D body1, Object3D body2) {
        float m_Impulse = (-(1 + mCoefficientOfRestitution) *
                (mRelativeVelocity.dotProduct(mCollisionNormal))) /
                ((1 / body1.getObjectPhysics().getMass() + 1 / body2.getObjectPhysics().getMass()));
        Vector3 force1 = Vector3.multiply(mCollisionNormal,m_Impulse);
        Vector3 force2 = Vector3.multiply(mCollisionNormal,-m_Impulse);

        body1.getObjectPhysics().applyTranslationalForce(force2);
        body2.getObjectPhysics().applyTranslationalForce(force1);
    }


    public float getMass() {
        return mMass;
    }


    public Vector3 getVelocity() {
        return mLinearVelocity;
    }
}
