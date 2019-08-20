package com.smzh.beautysdk;

public class FaceDetector {

    static {
        System.loadLibrary("beauty");
    }

    public native int init(String modelPath);

    public native Face[] detect(byte[] data, int type, int width, int height, int orientation);

    public native int destroy();

}
