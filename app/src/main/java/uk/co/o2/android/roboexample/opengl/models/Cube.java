package uk.co.o2.android.roboexample.opengl.models;

import android.content.Context;

/**
 * Created by hostova1 on 08/09/2014.
 */
public class Cube extends Object3D {

    public static float[] cubeData = {
           // x,     y,     z,    u,    v	 nx, ny,  nz
            -0.5f,  0.5f,  0.5f, 0.0f, 0.0f, -1,  1,  1,    // front top left      	0
            -0.5f, -0.5f,  0.5f, 0.0f, 1.0f, -1, -1,  1,    // front bottom left   	1
             0.5f, -0.5f,  0.5f, 1.0f, 1.0f,  1, -1,  1,    // front bottom right	2
             0.5f,  0.5f,  0.5f, 1.0f, 0.0f,  1,  1,  1,    // front top right		3
            -0.5f,  0.5f, -0.5f, 0.0f, 0.0f, -1,  1, -1,    // back top left		4
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, -1, -1, -1,    // back bottom left		5
             0.5f, -0.5f, -0.5f, 1.0f, 1.0f,  1, -1, -1,    // back bottom right	6
             0.5f,  0.5f, -0.5f, 1.0f, 0.0f,  1,  1, -1     // back top right    	7
    };

    public static float cubeDataNoTexture[] = {
            // x,     y,    z,   nx,  ny, nz
            -0.5f,  0.5f,  0.5f, -1,  1,  1,    // front top left      	0
            -0.5f, -0.5f,  0.5f, -1, -1,  1,    // front bottom left   	1
             0.5f, -0.5f,  0.5f,  1, -1,  1,    // front bottom right	2
             0.5f,  0.5f,  0.5f,  1,  1,  1,    // front top right		3
            -0.5f,  0.5f, -0.5f, -1,  1, -1,    // back top left		4
            -0.5f, -0.5f, -0.5f, -1, -1, -1,    // back bottom left		5
             0.5f, -0.5f, -0.5f,  1, -1, -1,    // back bottom right	6
             0.5f,  0.5f, -0.5f,  1,  1, -1     // back top right    	7
    };

    public static float CubeData4Sided[] ={
            // x,     y,    z,    u,      v     nx,  ny, nz
            -0.5f,  0.5f,  0.5f, 0.0f,  0.0f,   -1,  1,  1,  // front top left      0
            -0.5f, -0.5f,  0.5f, 0.0f,  1.0f,   -1, -1,  1,  // front bottom left   1
             0.5f, -0.5f,  0.5f, 1.0f,  1.0f,    1, -1,  1,  // front bottom right  2
             0.5f,  0.5f,  0.5f, 1.0f,  0.0f,    1,  1,  1,  // front top right     3

            -0.5f,  0.5f, -0.5f, 1.0f,   0.0f,  -1,  1, -1,  // back top left       4
            -0.5f, -0.5f, -0.5f, 1.0f,   1.0f,  -1, -1, -1,  // back bottom left    5
             0.5f, -0.5f, -0.5f, 0.0f,   1.0f,   1, -1, -1,  // back bottom right   6
             0.5f,  0.5f, -0.5f, 0.0f,   0.0f,   1,  1, -1   // back top right      7
    };


    public static final short cubeDrawOrder[] = {
            0, 3, 1, 3, 2, 1,    // Front panel
            4, 7, 5, 7, 6, 5,    // Back panel
            4, 0, 5, 0, 1, 5,    // Side
            7, 3, 6, 3, 2, 6,    // Side
            4, 7, 0, 7, 3, 0,    // Top
            5, 6, 1, 6, 2, 1     // Bottom
    }; // order to draw vertices

    public Cube( MeshEx meshEx, Texture[] textures, Material material, Shader shader) {
        super( meshEx, textures, material, shader);
    }
}
