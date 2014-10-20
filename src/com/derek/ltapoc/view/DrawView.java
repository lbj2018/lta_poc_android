package com.derek.ltapoc.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	private static final String TAG = "DrawView";
	private Bitmap mBitmap;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private Path mPath;
	private PointF mCurrentPoint;
	private Canvas mCanvas;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mPath = new Path();
		
		mPaint = new Paint();		
		mPaint.setAntiAlias(true);
	    mPaint.setDither(true);
	    mPaint.setColor(Color.GREEN);
	    mPaint.setStyle(Paint.Style.STROKE);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeWidth(12);  
		
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
	}

	public void drawForTouchDown(PointF point) {
		mPath.reset();
		
		mPath.moveTo(point.x, point.y);
		
		mCurrentPoint = point;
	}

	public void drawForTouchMove(PointF point) {
		mPath.moveTo(mCurrentPoint.x, mCurrentPoint.y);
		mPath.lineTo(point.x, point.y);	
		
		mCanvas.drawPath(mPath, mPaint); 
		
		mCurrentPoint = point;
	}
	
	public void drawForTouchUp(PointF point) {
		mPath.moveTo(mCurrentPoint.x, mCurrentPoint.y);
		mPath.lineTo(point.x, point.y);	
		
		mCanvas.drawPath(mPath, mPaint); 
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		PointF point = new PointF(x, y);

		Log.i(TAG, "X = " + x + ", Y = " + y);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawForTouchDown(point);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			drawForTouchMove(point);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			drawForTouchUp(point);
			invalidate();
			break;
		}

		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}
}
