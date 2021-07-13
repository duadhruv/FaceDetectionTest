package com.hex.facedetectiontest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class FaceOverlay extends View {
    public float top_space;
    public static float canvasHeight,canvasWidth;
    public static float omx, omnx,omy,omny;
    public static float topvalue=-1.0f;
    public FaceOverlay(Context context) {
        super(context);
        initView();
    }

    public FaceOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FaceOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public FaceOverlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    @Override
    protected void onDraw(Canvas canvas) {



        super.onDraw(canvas);
        //invalidate();
    }

    public void initView(){
        this.setLayerType(View.LAYER_TYPE_HARDWARE,null);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        top_space = 0.1f*canvas.getHeight();


        float heightRad,widthRadius;
        heightRad = 0.3f*canvasWidth;
        widthRadius = 0.2f*canvasWidth;

        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setColor(Color.TRANSPARENT);

        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));




        float right,left,top,bottom;
        left = (canvasWidth-(2*widthRadius))/2;
        right = left+(2*widthRadius);
        top = top_space;
        bottom = top+(2*heightRad);

        RectF rect = new RectF(left,top,right,bottom);

        float[] corners = new float[]{

                widthRadius*2f, widthRadius*2f,          // Bottom right radius in px
                widthRadius*2f, widthRadius*2f,
                widthRadius*1.9f, heightRad*2.2f,          // Bottom right radius in px
                widthRadius*1.9f, heightRad*2.2f
        };

        final Path path = new Path();
        path.addRoundRect(rect, corners, Path.Direction.CW);
        canvas.drawPath(path, eraser);

        //Drawing stroke
        Paint mPaint = new Paint();
        Path mPath=new Path();

        mPath.addRoundRect(rect,corners,Path.Direction.CW);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{15, 15, 15, 15}, 0));
        canvas.drawPath(mPath,mPaint);



        float padding = widthRadius*0.2f;
        omnx = left - padding;
        omny = top - padding;
        omx = right+padding;
        omy = bottom+padding;
        topvalue = top_space+(heightRad);


    }

}
