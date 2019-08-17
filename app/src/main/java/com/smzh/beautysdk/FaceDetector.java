package com.smzh.beautysdk;

public class FaceDetector {

    static {
        System.loadLibrary("beauty");
    }

    public native int init(String modelPath);

    //人脸特征标记
    public native int landMarks2(long input,long output);

    public native Face[] detect(byte[] data, int type, int width, int height, int orientation);

    public native int destroy();

}
