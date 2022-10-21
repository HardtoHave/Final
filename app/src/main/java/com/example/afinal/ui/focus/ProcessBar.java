package com.example.afinal.ui.focus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.afinal.R;

public class ProcessBar extends View {
    private final Paint paint;
    private int roundColor;
    private int roundProgressColor;
    private int circleBackgroundColor;
    private float roundWidth;
    private int oldx,oldy;
    private int max;        //max progress
    private int progress;   //current progress
    private final int textColor;  //center progress text color
    private final float textSize; //center progress text size
    private float pointRadius;
    private final float pointWidth;
    private final Drawable mThumb;
    private final Drawable mThumbPress;

    private static boolean flagLock = false;
    private progressCallback mProgressCallback;

    public ProcessBar(Context context) {
        this(context, null);
    }
    public ProcessBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public ProcessBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        @SuppressLint("CustomViewStyleable") TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        circleBackgroundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_circleBackGround, Color.YELLOW);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 3);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.WHITE);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_imageMax, 7200);
        //init, 7200 = 7200* (120min/120min)
        progress = 7200;
        pointRadius = mTypedArray.getDimension(R.styleable.RoundProgressBar_pointRadius, 3);
        pointWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_pointWidth, 2);
        mTypedArray.recycle();
        // load move icon
        mThumb = getResources().getDrawable(R.drawable.press_up);// 圆点图片
        int thumbHalfheight = mThumb.getIntrinsicHeight() / 2;
        int thumbHalfWidth = mThumb.getIntrinsicWidth() / 2;
        mThumb.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
        mThumbPress = getResources().getDrawable(R.drawable.press_down);// 圆点图片
        thumbHalfheight = mThumbPress.getIntrinsicHeight() / 2;
        thumbHalfWidth = mThumbPress.getIntrinsicWidth() / 2;
        mThumbPress.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
        paddingOuterThumb = thumbHalfheight;
    }
    @Override
    public void onDraw(Canvas canvas) {
        //draw the circle background
//        paint.setColor(circleBackgroundColor);
//        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);  //eliminate jaggies
//        canvas.drawCircle(centerX, centerY, radius-roundWidth/2, paint);

        @SuppressLint("DrawAllocation") RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);  //用于定义的圆弧的形状和大小的界限
        PointF progressPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 360 * progress / max, 270);
        if(!flagLock) {
            // draw outest circle
            paint.setColor(roundColor); //set circle color
            paint.setStyle(Paint.Style.STROKE); //set empty
            paint.setStrokeWidth(roundWidth); //set circle width
            canvas.drawCircle(centerX, centerY, radius, paint);

            //draw circular arc and its progress
            paint.setStrokeWidth(roundWidth + 2); //set arc width
            paint.setColor(roundProgressColor);  //set peogress color
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(oval, 270, 360 * progress / max, false, paint);  //根据进度画圆弧
            // teo points on the circle
            paint.setStrokeWidth(pointWidth);
            // PointF startPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 0, 270);
            // canvas.drawCircle(startPoint.x, startPoint.y, pointRadius, paint);
            //paint.setStyle(Paint.Style.FILL_AND_STROKE);
            //canvas.drawCircle(progressPoint.x, progressPoint.y, pointRadius, paint);
        }

        //text
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        // paint.setTypeface(Typeface.DEFAULT); //set text
        String textTime = getTimeText(progress);
        float textWidth = paint.measureText(textTime);   //measure text width
//        canvas.drawText(textTime, centerX - textWidth / 2, 160 + radius + centerY + textSize/2, paint);

        // draw Thumb
        canvas.save();
        canvas.translate(progressPoint.x, progressPoint.y);
        if(!flagLock) {
            if (downOnArc) {
                mThumbPress.draw(canvas);
            } else {
                mThumb.draw(canvas);
            }
        }
        canvas.restore();
    }
    private boolean downOnArc = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if(!flagLock) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (isTouchArc(x, y)) {
                        downOnArc = true;
                        updateArc(x, y);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (downOnArc) {
                        updateArc(x, y);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    downOnArc = false;
                    invalidate();
                    if (changeListener != null) {
                        changeListener.onProgressChangeEnd(max, progress);
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }
    private int centerX, centerY;
    private int radius;
    private final int paddingOuterThumb;
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        centerX = width / 2;
        centerY = height / 2;
        int minCenter = Math.min(centerX, centerY);
        radius = (int) (minCenter - roundWidth/2 - paddingOuterThumb)*3/4; //圆环的半径
        minValidateTouchArcRadius = (int) (radius - paddingOuterThumb*1.5f);
        maxValidateTouchArcRadius = (int) (radius + paddingOuterThumb*1.5f);
        super.onSizeChanged(width, height, oldw, oldh);
    }
    // update progress
    private void updateArc(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        // calculate angular，（-1->1）=（-180°->180°）
        double oldAngle = Math.atan2(oldy, oldx)/Math.PI;
        double angle = Math.atan2(cy, cx)/Math.PI;
        Log.d("oldAngle:",oldAngle+"");
        Log.d("Angle:",angle+"");
        // change angular to number between（0->2, add 90 degree offset
        if(oldAngle<=-0.5 && angle>-0.5 && angle < 0.5){
            angle = 2;
        }
        else{
            angle = ((2 + angle)%2 + (90/180f))%2;
            oldx = cx;
            oldy = cy;
        }

        int timeBlock = (int)(angle * 120 / 2);
        if(timeBlock <=10)
            timeBlock = 5;
        if(timeBlock <=117)
            timeBlock = Math.min(timeBlock / 5 * 5 + 5, 120);
        else
            timeBlock = 120;
        Log.d("time:",timeBlock+"");
        //current progress=angular number x total progress
        progress = timeBlock * max/120;
        if (changeListener != null) {
            changeListener.onProgressChange(max, progress);
        }
        invalidate();
        mProgressCallback.updateListener(progress);
    }

    //progress callback
    public interface progressCallback{
        void updateListener(int _progress);
    }
    public void setProgressCallback(progressCallback progressCallback){
        this.mProgressCallback = progressCallback;
    }

    private int minValidateTouchArcRadius; // set minimum clickable radius
    private int maxValidateTouchArcRadius; // set maximum clickable radius
    // judge click
    private boolean isTouchArc(int x, int y) {
        double d = getTouchRadius(x, y);
        return d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius;
    }
    // calculate distance point to origin
    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }
    public String getTimeText(int progress) {
        int minute = progress / 60;
        int second = progress % 60;
        return (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
    }
    public synchronized int getMax() {
        return max;
    }
    /**
     * set peogress maximum
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }
    /**
     * get progress
     */
    public synchronized int getProgress() {
        return progress;
    }
    /**
     * set synchronized progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        this.progress = progress;
        postInvalidate();
    }
    public int getCricleColor() {
        return roundColor;
    }
    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }
    public int getCricleProgressColor() {
        return roundProgressColor;
    }
    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }
    public float getRoundWidth() {
        return roundWidth;
    }
    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
    public static class ChartUtil {
        /**
         * Calculate the xy coordinates of the intersection of
         * the sector end ray and the arc according to the center coordinates, radius and sector angle.
         *
         */
        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle){
            float posX = 0.0f;
            float posY = 0.0f;
            //angle to radian conversion
            float arcAngle = (float) (Math.PI * cirAngle / 180.0);
            if (cirAngle < 90)
            {
                posX = cirX + (float)(Math.cos(arcAngle)) * radius;
                posY = cirY + (float)(Math.sin(arcAngle)) * radius;
            }
            else if (cirAngle == 90)
            {
                posX = cirX;
                posY = cirY + radius;
            }
            else if (cirAngle > 90 && cirAngle < 180)
            {
                arcAngle = (float) (Math.PI * (180 - cirAngle) / 180.0);
                posX = cirX - (float)(Math.cos(arcAngle)) * radius;
                posY = cirY + (float)(Math.sin(arcAngle)) * radius;
            }
            else if (cirAngle == 180)
            {
                posX = cirX - radius;
                posY = cirY;
            }
            else if (cirAngle > 180 && cirAngle < 270)
            {
                arcAngle = (float) (Math.PI * (cirAngle - 180) / 180.0);
                posX = cirX - (float)(Math.cos(arcAngle)) * radius;
                posY = cirY - (float)(Math.sin(arcAngle)) * radius;
            }
            else if (cirAngle == 270)
            {
                posX = cirX;
                posY = cirY - radius;
            }
            else if(cirAngle > 270 && cirAngle < 360)
            {
                arcAngle = (float) (Math.PI * (360 - cirAngle) / 180.0);
                posX = cirX + (float)(Math.cos(arcAngle)) * radius;
                posY = cirY - (float)(Math.sin(arcAngle)) * radius;
            }
            else if(cirAngle >= 360){
                posX = cirX + radius;
                posY = cirY;
            }
            return new PointF(posX, posY);
        }
        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle, float orginAngle){
            if(orginAngle + cirAngle < 630)
                cirAngle = (orginAngle + cirAngle) % 360;
            else
                cirAngle = orginAngle;
            return calcArcEndPointXY(cirX, cirY, radius, cirAngle);
        }
    }
    private OnProgressChangeListener changeListener;
    public void setChangeListener(OnProgressChangeListener changeListener) {
        this.changeListener = changeListener;
    }
    public interface OnProgressChangeListener {
        void onProgressChange(int duration, int progress);
        void onProgressChangeEnd(int duration, int progress);
    }

    public boolean getflagLock(){
        return flagLock;
    }
    public void setflagLock(boolean flagLock){
        ProcessBar.flagLock = flagLock;
    }
}
