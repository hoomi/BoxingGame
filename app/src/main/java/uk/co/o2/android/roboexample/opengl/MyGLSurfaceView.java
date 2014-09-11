package uk.co.o2.android.roboexample.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context) {
        super(context);
        initMyView(context);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView(context);
    }

    private void initMyView(Context context) {
        setEGLContextClientVersion(2);
        setRenderer(new MyGLRenderer(context));
//        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
