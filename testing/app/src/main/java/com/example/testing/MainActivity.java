package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

import org.opencv.imgcodecs.Imgcodecs;

public class MainActivity extends AppCompatActivity {


    String filename1 = "/home/atg0831/1.jpeg";
    String filename2 = "/home/atg0831/2.jpeg";
   int ret=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("dad");
        System.out.println(filename1);
        TextView textView=(TextView)findViewById(R.id.text1);
        textView.setText(filename1);
        ret=compareFeature(filename1,filename2);

        if (ret > 0) {
            textView.setText("그림이 같다");
        } else {
            textView.setText("그림이 같지 다");
        }

    }

    public  static int compareFeature(String filename1,String filename2)
    {
        int retVal=0;
//        long startTime=
        Mat img1=Imgcodecs.imread(filename1,Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat img2=Imgcodecs.imread(filename2,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        MatOfKeyPoint keypoints1=new MatOfKeyPoint();
        MatOfKeyPoint keypoints2=new MatOfKeyPoint();

        Mat descriptors1=new Mat(h, w, CvType.CV_8UC1, y_plane);
        Mat descriptors2=new Mat(h, w, CvType.CV_8UC1, y_plane);

        FeatureDetector detector=FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor extractor=DescriptorExtractor.create(DescriptorExtractor.ORB);

        detector.detect(img1,keypoints1);
        detector.detect(img2,keypoints2);

        extractor.compute(img1,keypoints1, descriptors1);
        extractor.compute(img2,keypoints2, descriptors2);

        DescriptorMatcher matcher=DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        MatOfDMatch matches=new MatOfDMatch();

        if(descriptors2.cols()==descriptors1.cols()){
            matcher.match(descriptors1,descriptors2,matches);

            DMatch[] match=matches.toArray();
            double max_dist=0; double min_dist=100;

            for(int i=0;i<descriptors1.rows();i++){
                double dist=match[i].distance;
                if(dist<min_dist)min_dist=dist;
                if(dist>max_dist)max_dist=dist;

            }
            System.out.println("max_dist="+ max_dist+ "min_dist="+min_dist);

            for(int i=0;i<descriptors1.rows();i++)
            {
                if(match[i].distance<=10){
                    retVal++;
                }
            }
            System.out.println("matching count="+retVal);
        }


        return retVal;


    }

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;


    protected void onCameraPermissionGranted() {

    }
}