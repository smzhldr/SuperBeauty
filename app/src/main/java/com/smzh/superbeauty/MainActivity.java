package com.smzh.superbeauty;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.smzh.beautysdk.CommonUtils;


public class MainActivity extends AppCompatActivity {

    private Button imageButton;
    private Button videoButton;
    private ProgressBar progressBar;

    private boolean init;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.image);
        videoButton = findViewById(R.id.video);
        progressBar = findViewById(R.id.progress_bar);

        imageButton.setEnabled(false);
        videoButton.setEnabled(false);

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 90);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progressBar.setProgress(value);
            }
        });

        valueAnimator.setDuration(3000);
        valueAnimator.start();

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return CommonUtils.copyFaceShape68ModelFile(getApplicationContext());
            }

            @Override
            protected void onPostExecute(Boolean init) {
                if (init) {
                    imageButton.setEnabled(true);
                    videoButton.setEnabled(true);
                    valueAnimator.cancel();
                    progressBar.setProgress(100);
                    progressBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, 500);
                    Toast.makeText(MainActivity.this, "init success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "init failed", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });


        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(intent);
            }
        });
    }
}
