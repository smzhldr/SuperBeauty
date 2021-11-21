package com.smzh.beauty.facedetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class CommonUtils {

    static String faceShape68ModelName = "shape_predictor_68_face_landmarks.dat";


    public static boolean copyModeToSDCard(@NonNull final Context context) {
        File file = new File(getFaceShape68ModelPath(context));
        if (file.exists()) {
            return true;
        }
        try {
            FileOutputStream out = new FileOutputStream(getFaceShape68ModelPath(context));
            InputStream in = context.getAssets().open(faceShape68ModelName);
            byte[] buff = new byte[1024];
            int read;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            in.close();
            out.close();
            return true;
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

    public static String getFaceShape68ModelPath(Context context) {
       return context.getExternalFilesDir(null) + File.separator + faceShape68ModelName;
    }


    public static int dpToPx(Context context,float dpValue) {//dp转换为px
        float scale=context.getResources().getDisplayMetrics().density;//获得当前屏幕密度
        return (int)(dpValue*scale+0.5f);
    }
}
