package com.hex.facedetectiontest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class FaceOverlay extends View {
    public float top_space, overlay_height,overlay_width;
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
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getContext().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//
//        this.he


    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAlpha(80);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        top_space = 0.2f*canvas.getHeight();
        overlay_height = 0.5f*canvas.getWidth();
        overlay_width = 0.25f*canvas.getWidth();
        float cx = canvas.getWidth()/2;
        float cy = top_space+(overlay_height/2);
        float radius = overlay_width/2;

        Paint paintt = new Paint();
        paintt.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paintt.setColor(Color.TRANSPARENT);
        canvas.drawCircle(cx, cy, radius, paintt);

    }

}
