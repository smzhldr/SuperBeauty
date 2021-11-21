package com.smzh.beauty.facedetector;

public class FaceDetector {

    public static int TYPE_RGB8888 = 1;
    public static int TYPE_RGB_565 = 2;
    public static int TYPE_NV21 = 3;

    static {
        System.loadLibrary("facedetector");
    }

    private long instance = 0;

    public boolean createFaceDetector(String modePath) {
        instance = create(modePath);
        return instance != 0;
    }

    public Face[] detectImage(byte[] data, int type, int width, int height) {
        if (instance !=0) {
            return detect(instance, data, type, width, height);
        }
        return  null;
    }

    public void destroyFaceDetector() {
        if (instance != 0) {
            destroy(instance);
            instance = 0;
        }
    }

    private native long create(String modelPath);

    private native Face[] detect(long instance, byte[] data, int type, int width, int height);

    private native int destroy(long instance);

}
