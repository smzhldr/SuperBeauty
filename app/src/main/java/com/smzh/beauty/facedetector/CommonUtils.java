package com.smzh.beauty.facedetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class CommonUtils {

    static String faceShape68ModelName = "shape_predictor_68_face_landmarks.dat";


    private static boolean copyFileFromAssetsToOthers(@NonNull final Context context, @NonNull final String fileName, @NonNull final String targetPath) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = context.getAssets().open(fileName);
            out = new FileOutputStream(targetPath);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean copyFaceShape68ModelFile(@NonNull final Context context) {
        final String targetPath = getFaceShape68ModelPath();
        try {
            File file = new File(targetPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
                return CommonUtils.copyFileFromAssetsToOthers(context.getApplicationContext(), faceShape68ModelName, targetPath);
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    public static byte[] bitmap2bytes(Bitmap bitmap) {
        int size1 = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size1);
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }

    public static String getFaceShape68ModelPath() {
        File sdcard = Environment.getExternalStorageDirectory();
        String directory = "model";
        return sdcard.getAbsolutePath() + File.separator + directory + File.separator + faceShape68ModelName;
    }

}
