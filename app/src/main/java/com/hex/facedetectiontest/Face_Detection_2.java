package com.hex.facedetectiontest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.FileDescriptor;
import java.io.IOException;

public class Face_Detection_2 extends AppCompatActivity {
    private static final int pic_id = 123;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection2);
        getSupportActionBar().hide();
        imageView = findViewById(R.id.imageview);
       openCamera();
    }


    void openCamera(){
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (requestCode == pic_id) {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                Uri selectedImageUri = data.getData();
                try {
                    showImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

//    private void getBitMapFromUri(Uri uri) throws IOException{
//        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
//        FileDescriptor fileDescriptor=parcelFileDescriptor.getFileDescriptor();
//        showImage(fileDescriptor);
//
//    }

    private void showImage(Bitmap bmp) {
//        BitmapFactory.Options options=new BitmapFactory.Options();
//        options.inMutable = true;
        Bitmap myBitmap=bmp;
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(1);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        FaceDetector faceDetector = new
                FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                .build();

        if(!faceDetector.isOperational()) {
            //Toast.makeText(getApplicationContext(),"Failed to get face",Toast.LENGTH_SHORT).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        for (int i = 0; i < faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            //Toast.makeText(getApplicationContext(),"Failed to get face",Toast.LENGTH_SHORT).show();

        }
        imageView.setImageBitmap(tempBitmap);

    }
}