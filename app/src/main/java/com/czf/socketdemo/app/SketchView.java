package com.czf.socketdemo.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

/**
 * sketch.
 * Created by czf on 14/05/2018.
 */

public class SketchView extends View {

    private Paint paint;
    private String color;
    private float mX, mY;

    SketchView(Context context) {
        this(context, null);
    }

    SketchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    SketchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(100);
        color = "#00ff00";
        paint.setColor(Color.parseColor(color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint(mX, mY, paint);
    }

    public void moveToPoint(float x, float y) {
        mX = x;
        mY = y;
        invalidate();
    }
}
