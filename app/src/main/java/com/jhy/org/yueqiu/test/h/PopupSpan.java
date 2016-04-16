package com.jhy.org.yueqiu.test.h;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/16 0016.
 */
public class PopupSpan extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static class SpanInfo {
        private int bgImage = R.drawable.icon_sidebar_team;
        private int bgColor = 0xAA298EBD; //背景颜色

        private int padding = 4;
        private int width = 4;
        private int diameter = 0; //直径
        private int radius = 0; //半径

        private float paddingAngle = 3; //中间的夹角
        private float startAngle = 180; //开始角度
        private float endAngle = 360; //结束角度

        private String[] texts = new String[] {"1V1比赛", "组队比赛", "娱乐练习"};
        private int textSize = 20;
        private int textColor = 0xFFFFFFFF;

        private int linkColor = 0xAAC9C1AD; //链接颜色
        private int activeColor = 0xAAA98A3F; //激活颜色
    }

    private Thread thread;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint arcPaint;
    private Paint textPaint;
    private Bitmap bgImage;
    private RectF range;
    private RectF clipRange;
    private SpanInfo info = new SpanInfo();

    public PopupSpan(Context context) {
        this(context, null);
    }
    public PopupSpan(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        info.width = Math.min(getMeasuredWidth(), getMeasuredHeight() * 2);
        info.diameter = info.width - info.padding * 2;
        info.radius = info.diameter / 2;
        setMeasuredDimension(info.width, info.width / 2);

        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true); // 抗锯齿
        arcPaint.setDither(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true); // 抗锯齿
        textPaint.setDither(true);
        textPaint.setColor(info.textColor);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, info.textSize, getResources().getDisplayMetrics()));

        float start = info.padding;
        float end = info.width - info.padding;
        range = new RectF(start, start, end, end);
        start += info.radius / 2;
        end -= info.radius / 2;
        clipRange = new RectF(start, start, end, end);
        bgImage = BitmapFactory.decodeResource(getResources(), info.bgImage);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        draw();
    }

    private void draw () {
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                drawBackground();
                int itemCount = info.texts.length;
                float tempAngle = info.startAngle + info.paddingAngle / 2;
                float sweepAngle = (info.endAngle - info.startAngle) / itemCount - info.paddingAngle;
                for (int i = 0; i < itemCount; i++) {
                    drawArc(tempAngle, sweepAngle, info.texts[i], info.activeColor);

                    tempAngle += sweepAngle + info.paddingAngle;
                }
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawBackground () {
//        Bitmap bitmap = Bitmap.createBitmap(info.diameter, info.diameter, Bitmap.Config.ARGB_4444);
//        bitmap.eraseColor(Color.argb(0,0,0,0));
//        canvas.drawBitmap(bitmap, info.padding, info.padding, null);
//        canvas.drawColor(0xFFFFFFFF, PorterDuff.Mode.OVERLAY);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    private void drawArc (float startAngle, float sweepAngle, String text, int color) {
//        Log.i("ilog:", "startAngle:" + startAngle);
//        arcPaint.setColor(color);
//        canvas.drawArc(range, startAngle, sweepAngle, true, arcPaint);
//        arcPaint.setColor(Color.TRANSPARENT);
//        canvas.drawArc(clipRange, startAngle, sweepAngle, true, arcPaint);

        arcPaint.setColor(color);
        Path arcPath = new Path();
        arcPath.addArc(range, startAngle, sweepAngle);
        arcPath.arcTo(clipRange, startAngle + sweepAngle, -sweepAngle);
        canvas.drawPath(arcPath, arcPaint);

        Path textPath = new Path();
        textPath.addArc(range, startAngle, sweepAngle);

        float textWidth = textPaint.measureText(text);
        float offsetX = (float)(info.radius * sweepAngle / 180 * Math.PI - textWidth) / 2;
        float offsetY = info.radius / 4;
        canvas.drawTextOnPath(text, textPath, offsetX, offsetY, textPaint);
    }
}
