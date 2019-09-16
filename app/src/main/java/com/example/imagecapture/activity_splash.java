package com.example.imagecapture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import static java.lang.Thread.sleep;

public class activity_splash extends AppCompatActivity {

    private ProgressBar spinner;
    ImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //progress bar
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        splashImage=(ImageView)findViewById(R.id.load);

        Animation newanimation = AnimationUtils.loadAnimation(this,R.anim.splashanim);
        splashImage.startAnimation(newanimation);

        //thread to set time for the splash to be shown
        final Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(3000);
                    Intent i = new Intent(activity_splash.this, MainActivity.class);
                    startActivity(i);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        myThread.start();


    }
}
