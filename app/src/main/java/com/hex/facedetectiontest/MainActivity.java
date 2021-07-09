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

import android.graphics.ImageFormat;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FaceDetector faceDetector;
    RectanglesView rectanglesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rectanglesView = findViewById(R.id.rectanglesView);
        CameraView cameraView = findViewById(R.id.cameraView);


        faceDetector = FaceDetector.create(this);

        CustomFrameProcessor processor = new CustomFrameProcessor();


        Fotoapparat.with(this)
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

            runOnUiThread(() -> rectanglesView.setRectangles(faces));
            //faces.get(0).getX()
        }
    }
}