package com.example.gubonwoo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

public class compareActivity extends AppCompatActivity {


    Button Button_camera;
    ImageView imageView;
    Button button;
    Button dialog;

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
//        btn_search=(Button)findViewById(R.id.search);
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
        dialog=(Button)findViewById(R.id.search) ;
        dialog.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View view){
                    if(view==dialog){ //view가 alert 이면 팝업실행 즉 버튼을 누르면 팝업창이 뜨는 조건
                        Context mContext = getApplicationContext();
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                        //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
                        View layout = inflater.inflate(R.layout.dialog,(ViewGroup)findViewById(R.id.popup));
                        AlertDialog.Builder aDialog = new AlertDialog.Builder(compareActivity.this);

                        aDialog.setTitle("Warning"); //타이틀바 제목
                        aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅

                        //그냥 닫기버튼을 위한 부분
                        aDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        //팝업창 생성
                        AlertDialog ad = aDialog.create();
                        ad.show();//보여줌!
                    }



//                Dialog();
            }

        });

    }

    public void Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고등");
        builder.setMessage("AlertDialog 내용");
        builder.setNeutralButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        }).show();
//        builder.setPositiveButton("우측버튼",new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"우측버튼 클릭됨",Toast.LENGTH_LONG).show();
//            }
//        });
//        builder.setNegativeButton("좌측버튼",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(),"좌측버튼 클릭됨",Toast.LENGTH_LONG).show();
//                    }
//                });
//        builder.show();
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
