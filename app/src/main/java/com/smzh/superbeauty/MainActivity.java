package com.smzh.superbeauty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.smzh.beautysdk.Constants;
import com.smzh.beautysdk.Face;
import com.smzh.beautysdk.FaceDetector;
import com.smzh.beautysdk.FileUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;


public class MainActivity extends AppCompatActivity {

    private ImageView img;
    private Bitmap srcBitmap;
    private Handler handler = new Handler();
    private FaceDetector faceDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVLoader.initDebug(false);

        img = findViewById(R.id.imageView);
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face_photo2);
        img.setImageBitmap(srcBitmap);

        faceDetect = new FaceDetector();

        Button button = findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFaceLandmark();
            }
        });
    }


    //68点检测
    private void callFaceLandmark() {
        new Thread(new Runnable() {
            public void run() {
                FileUtils.copyFaceShape68ModelFile(getApplicationContext());
                faceDetect.init(Constants.getFaceShape68ModelPath());
                long sDetectTime = System.currentTimeMillis();
                Mat input = new Mat();
                Mat output = new Mat();
                Utils.bitmapToMat(srcBitmap, input);
                faceDetect.landMarks2(input.getNativeObjAddr(), output.getNativeObjAddr());

                Face[] faces = faceDetect.detect(bitmap2bytes(srcBitmap),0,srcBitmap.getWidth(),srcBitmap.getHeight(),0);
                Utils.matToBitmap(output, srcBitmap);
                long detectTime = System.currentTimeMillis() - sDetectTime;
                String detectTimeStr = "检测68点,耗时:" + String.valueOf(detectTime) + "ms.";
                Log.d("detect_time", detectTimeStr);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(srcBitmap);
                    }
                });

            }
        }).start();
    }

    private byte[] bitmap2bytes(Bitmap bitmap) {
        int size1 = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size1);
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }
}
