package com.hex.facedetectiontest;

import androidx.appcompat.app.AppCompatActivity;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.characteristic.LensPosition;
import io.fotoapparat.facedetector.FaceDetector;
import io.fotoapparat.facedetector.Rectangle;
import io.fotoapparat.facedetector.processor.FaceDetectorProcessor;
import io.fotoapparat.facedetector.view.CameraOverlayLayout;
import io.fotoapparat.facedetector.view.RectanglesView;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.preview.FrameProcessor;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.view.CameraView;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.vision.face.Face;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FaceDetector faceDetector;
    RectanglesView rectanglesView;
    Fotoapparat f;
    TextView co;
    public int rectheight = ScannerOverlay.rectHeight;
    public int rectwidth = ScannerOverlay.rectWidth;
    TextView face_not_detecting;
    LottieAnimationView captured;
    String y="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        rectanglesView = findViewById(R.id.rectanglesView);
        CameraView cameraView = findViewById(R.id.cameraView);
        co = findViewById(R.id.co);
        face_not_detecting = findViewById(R.id.face_not_detecting);
        captured = findViewById(R.id.captured);

        face_not_detecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Face_Detection_2.class));
                //finish();
            }
        });



        faceDetector = FaceDetector.create(this);

        CustomFrameProcessor processor = new CustomFrameProcessor();


        f.with(this)
                .lensPosition(LensPositionSelectorsKt.front())
                .into(cameraView)
                .frameProcessor(processor)
                .build()
                .start();

        findViewById(R.id.cap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = Environment.getExternalStorageState()+"/"+System.currentTimeMillis()+"_img.jpg";
                File file = new File(path);
                f.takePicture();
            }
        });
    }

    private class CustomFrameProcessor implements FrameProcessor {
        @Override
        public void process(@NotNull Frame frame) {


            List<Rectangle> faces = faceDetector.detectFaces(frame.getImage(), frame.getSize().width, frame.getSize().height, frame.getRotation());
            try{
               y = faces.get(0).getY()+"";

            }
            catch (Exception e){
               y = "";

            }

//            com.google.android.gms.vision.Frame f = new com.google.android.gms.vision.Frame.Builder().setImageData(ByteBuffer.wrap(frame.getImage()), frame.getSize().width, frame.getSize().height, ImageFormat.NV21).build();
//            detectFaces(f);

//            runOnUiThread(() -> rectanglesView.setRectangles(faces)
//
//            );
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rectanglesView.setRectangles(faces);
                    try{


                        if(y.trim()!=""){
                            captured.setVisibility(View.VISIBLE);
                            captured.loop(false);

                        }
                        else {
                           // captured.setVisibility(View.INVISIBLE);
                           // captured.loop(true);
                        }





                    float x = faces.get(0).getX();
                    float y = faces.get(0).getY();
                    float x_cent = (x + frame.getSize().width) / 2;
                    float y_cent = (y + frame.getSize().height) / 2;
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    int width = displayMetrics.widthPixels;

                    Log.i("CamH/W"," height = "+height/2+" width="+width/2);
                    Log.i("Co-ordinates","x = " + x + " Y = " + y + " x-cent = " + x_cent + " y-cent " + y_cent);
                    co.setText((width/2)+" x_cent = "+x_cent+"x = "+x);
//
//                    if((int)x_cent< && (int)y_cent<(height/2)){
//                        Log.i("Success---------->","Co-ordinates matched");
//                        co.setText("matched");
//                        Toast.makeText(getApplicationContext(),"Face match",Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        co.setText("Waiting for match");
//                    }
//
//
//
//                    float x1=0.0f,x2=0.0f,y1=0.0f,y2=0.0f;
//                    y1 = (float) ((height/2)-(0.25*(height/2)));
//                    y2 = (float) ((height/2)+(0.25*(height/2)));
//                    x1 = (float) ((width/2)-(0.25*(width/2)));
//                    x2 = (float) ((width/2)+(0.25*(width/2)));
//
//                    if(x_cent>x1 && x_cent<x2&&y_cent>y1&&y_cent<y2){
//
//                    }
//
//                int[] location = new int[2];
//                    View cent = findViewById(R.id.cent);
//                    cent.getLocationOnScreen(location);
//                    Log.e("Center co-ordinates","X axis is "+location[0] +"and Y axis is "+location[1]);
//                co.setText("X axis is "+location[0] +"and Y axis is "+location[1]);
//
//


                    }
                    catch (Exception e){

                        Log.i("Error",e.getMessage().toString());


                    }

                }
            });






        }
    }

//    public void detectFaces(com.google.android.gms.vision.Frame frame)
//    {
//        com.google.android.gms.vision.face.FaceDetector faceDetector = new
//                com.google.android.gms.vision.face.FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
//                .build();
//
//        if(!faceDetector.isOperational()) {
//            //Toast.makeText(getApplicationContext(),"Failed to get face",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        SparseArray<com.google.android.gms.vision.face.Face> faces = faceDetector.detect(frame);
//        Log.e("Faces Frame",faces.size()+"");
//
//
//        for (int i = 0; i < faces.size(); i++) {
//            Face thisFace = faces.valueAt(i);
//            float x1 = thisFace.getPosition().x;
//            float y1 = thisFace.getPosition().y;
//            float x2 = x1 + thisFace.getWidth();
//            float y2 = y1 + thisFace.getHeight();
//            //tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
//            //Toast.makeText(getApplicationContext(),"Failed to get face",Toast.LENGTH_SHORT).show();
//
//        }
//    }
    public static Point getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }
}