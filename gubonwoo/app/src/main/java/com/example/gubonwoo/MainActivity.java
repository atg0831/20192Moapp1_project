package com.example.gubonwoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Loading.class);
        startActivity(intent);


        ImageButton cbutton=(ImageButton) findViewById(R.id.compare_btn);
        cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(), compareActivity.class);
                startActivity(intent1);

            }
        });


        ImageButton mbutton=(ImageButton) findViewById(R.id.map_btn);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent1);

            }
        });

        ImageButton sbutton=(ImageButton) findViewById(R.id.search_btn);
        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent1);

            }
        });
    }


}
