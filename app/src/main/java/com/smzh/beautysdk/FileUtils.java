package com.smzh.beautysdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    @NonNull
    public static final void copyFileFromRawToOthers(@NonNull final Context context, @RawRes int id, @NonNull final String targetPath) {
        InputStream in = context.getResources().openRawResource(id);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(targetPath);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @NonNull
    public static final void copyFileFromAssetsToOthers(@NonNull final Context context, @NonNull final String fileName, @NonNull final String targetPath) {
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
        } catch (Exception e) {
            e.printStackTrace();
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

    @NonNull
    public static void copyFaceShape68ModelFile(@NonNull final Context context){
        final String targetPath = Constants.getFaceShape68ModelPath();
        try{
            File file = new File(targetPath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
                FileUtils.copyFileFromAssetsToOthers(context.getApplicationContext(), Constants.faceShape68ModelName, targetPath);
            }
        }catch (Exception e){

        }
    }

}
