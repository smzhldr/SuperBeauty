package com.smzh.superbeauty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.smzh.beautysdk.CommonUtils;
import com.smzh.beautysdk.Constants;
import com.smzh.beautysdk.Face;
import com.smzh.beautysdk.FaceDetector;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap srcBitmap;
    private FaceDetector faceDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView = findViewById(R.id.imageView);
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face_photo);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) ((float) srcBitmap.getHeight() / srcBitmap.getWidth() * width);
        srcBitmap = CommonUtils.scaleWithWH(srcBitmap, width, height);
        imageView.setImageBitmap(srcBitmap);

        faceDetect = new FaceDetector();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                faceDetect.init(Constants.getFaceShape68ModelPath());
            }
        });

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
        AsyncTask.execute(new Runnable() {
            public void run() {
                long sDetectTime = System.currentTimeMillis();
                final Face[] faces = faceDetect.detect(CommonUtils.bitmap2bytes(srcBitmap), 0, srcBitmap.getWidth(), srcBitmap.getHeight(), 0);
                long detectTime = System.currentTimeMillis() - sDetectTime;
                String detectTimeStr = "检测68点,耗时:" + detectTime + "ms.";
                Log.d("detect_time", detectTimeStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Canvas canvas = new Canvas(srcBitmap);
                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStrokeWidth(7);
                        Paint numPaint = new Paint();
                        numPaint.setColor(Color.GREEN);
                        numPaint.setStrokeWidth(20);
                        numPaint.setTextSize(20);
                        for (Face face : faces) {
                            PointF[] facePoint = face.getFacePoints();
                            for (int j = 0; j < facePoint.length; j++) {
                                canvas.drawPoint(facePoint[j].x, facePoint[j].y, paint);
                                canvas.drawText(String.valueOf(j), facePoint[j].x - 10, facePoint[j].y + 20, numPaint);
                            }
                        }
                        imageView.setImageBitmap(srcBitmap);
                    }
                });

            }
        });
    }
}
