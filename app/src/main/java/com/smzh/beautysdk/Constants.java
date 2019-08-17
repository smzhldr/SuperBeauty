package com.smzh.beautysdk;

import android.os.Environment;

import java.io.File;

public class Constants {

    static String faceShape68ModelName = "shape_predictor_68_face_landmarks.dat";

    /**
     * 68个人脸特征的人脸检测算法模型
     * @return string
     */
    public static String getFaceShape68ModelPath() {
        File sdcard = Environment.getExternalStorageDirectory();
        String directory = "model";
        return sdcard.getAbsolutePath() + File.separator + directory + File.separator + faceShape68ModelName;
    }
}
