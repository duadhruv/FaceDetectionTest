package com.hex.facedetectiontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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


    LottieAnimationView captured;
    String y="";
    float omx, omnx,omy,omny,fmx,fmny,fmy,fmnx;
    FaceOverlay faceOverlay;
    TextView message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        rectanglesView = findViewById(R.id.rectanglesView);
        CameraView cameraView = findViewById(R.id.cameraView);

        captured = findViewById(R.id.captured);
        faceOverlay = findViewById(R.id.faceoverlay);


        setHeightMargin();

        faceDetector = FaceDetector.create(this);
        message = findViewById(R.id.message);

        CustomFrameProcessor processor = new CustomFrameProcessor();


        f.with(this)
                .lensPosition(LensPositionSelectorsKt.front())
                .into(cameraView)
                .frameProcessor(processor)
                .build()
                .start();

    }

    private class CustomFrameProcessor implements FrameProcessor {
        @Override
        public void process(@NotNull Frame frame) {


            List<Rectangle> faces = faceDetector.detectFaces(frame.getImage(), frame.getSize().width, frame.getSize().height, frame.getRotation());

            omnx = faceOverlay.omnx;
            omx = faceOverlay.omx;
            omy = faceOverlay.omy;
            omny = faceOverlay.omny;
            Rectangle rectangle = new Rectangle(omnx/rectanglesView.getWidth(),omny/rectanglesView.getHeight(),(omx-omnx)/rectanglesView.getWidth(),(omy-omny)/rectanglesView.getHeight());
            faces.add(rectangle);



            if(!(faces.size() ==0)){
                for(int i = 0;i<faces.size();i++){
                    try{

                        final int left = (int) (faces.get(i).getX() * rectanglesView.getWidth());
                        final int top = (int) (faces.get(i).getY() * rectanglesView.getHeight());
                        final int right = left + (int) (faces.get(i).getWidth() * rectanglesView.getWidth());
                        final int bottom = top + (int) (faces.get(i).getHeight() * rectanglesView.getHeight());
                        fmnx = left;
                        fmx = right;
                        fmy = bottom;
                        fmny=top;


                        boolean isinside = (fmnx>=omnx)&&(fmny>=omny)&&(fmx<=omx)&&(fmy<=omy);
                        if(isinside==true){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onFaceScanned();
                                }
                            });

                        }
                        else {

                        }
                    }
                    catch (Exception e){

                        Log.i("Error",e.getMessage().toString());


                    }
                }

            }
        }
    }
    void setHeightMargin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (faceOverlay.topvalue==-1){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)captured.getLayoutParams();
                        params.setMargins(params.leftMargin, (int) (faceOverlay.topvalue+(params.height/2)),params.rightMargin,params.bottomMargin);
                        captured.setLayoutParams(params);
                        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams)message.getLayoutParams();
                        p.setMargins(params.leftMargin, (int) faceOverlay.omy+(int)faceOverlay.marginBotm,params.rightMargin,params.bottomMargin);
                        message.setLayoutParams(p);




                    }
                });
            }
        }).start();
    }

    void onFaceScanned(){
        captured.setVisibility(View.VISIBLE);
        captured.loop(false);
        faceOverlay.success();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //finish();
            }
        },1000);


    }
}