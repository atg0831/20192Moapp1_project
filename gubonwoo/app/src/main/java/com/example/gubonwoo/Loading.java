package com.example.gubonwoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Loading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        startLoading();
    }
    public void startBlinkingAnimation(View view) {
        ImageView image =  findViewById(R.id.blinking_animation);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);
        image.startAnimation(startAnimation);
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        }, 2000);
    }
}