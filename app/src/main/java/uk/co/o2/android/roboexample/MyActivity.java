package uk.co.o2.android.roboexample;

import android.app.Activity;
import android.os.Bundle;

import uk.co.o2.android.roboexample.opengl.MyGLSurfaceView;


public class MyActivity extends Activity {

    private MyGLSurfaceView myGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        myGLSurfaceView = (MyGLSurfaceView) findViewById(R.id.glSurfaceView);
    }

    @Override
    protected void onPause() {
        myGLSurfaceView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        myGLSurfaceView.onResume();
        super.onResume();
    }
}
