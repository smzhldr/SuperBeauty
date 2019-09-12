package com.smzh.beautysdk;

public class FaceDetector {

    public static int TYPE_RGB8888 = 1;
    public static int TYPE_RGB_565 = 2;
    public static int TYPE_NV21 = 3;

    static {
        System.loadLibrary("beauty");
    }

    public native int init(String modelPath);

    public native Face[] detect(byte[] data, int type, int width, int height, int orientation);

    public native int destroy();

}
