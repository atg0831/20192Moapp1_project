package com.example.gubonwoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;

public class compareActivity extends AppCompatActivity {


    Button Button_camera;
    ImageView imageView;
    Button button;
    Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

//        ImageButton button=(ImageButton) findViewById(R.id.homeButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//
//            }
//        });

        Button_camera=(Button)findViewById(R.id.camera);
        imageView=(ImageView)findViewById(R.id.image);
        button=(Button)findViewById(R.id.button);
        btn_search=(Button)findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
        Button_camera.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View view){
              Intent i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

              startActivityForResult(i,0);
          }
        });
//
//        btn_search.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//
////                EditText editTextFilter=(EditText)findViewById(R.id.editTextFilter);
//                Intent intent= new Intent(compareActivity.this,SearchActivity.class);
////                editTextFilter.setText("엔진/배출 경고등");
//                intent.putExtra("text","엔진/배출 경고등");
//
//                startActivity(intent);
//
//            }
//        });

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        imageView.setImageURI(data.getData());
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                try{
                    InputStream in=getContentResolver().openInputStream(data.getData());
                    Bitmap img= BitmapFactory.decodeStream(in);
                in.close();
                imageView.setImageBitmap(img);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
//
//    public File getAlbumDir(){
//        File storageDir=new File(
//                Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES
//
//                ),
//                getAlbumName()
//        );
//        return storageDir;
//    }
}
