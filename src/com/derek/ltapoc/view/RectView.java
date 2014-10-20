package com.derek.ltapoc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RectView extends View {
	private static final String TAG = "RectView";
	private Paint mPaint;
	private PointF mStartPoint;
	private PointF mEndPoint;
	
	public RectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mPaint = new Paint();		
		mPaint.setAntiAlias(true);
	    mPaint.setDither(true);
	    mPaint.setColor(Color.GREEN);
	    mPaint.setStyle(Paint.Style.FILL);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeWidth(12);  
	    
	    mStartPoint = mEndPoint = new PointF(0.0f, 0.0f); 
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mEndPoint.y < mStartPoint.y) {
			RectF rect = new RectF(0, mEndPoint.y, getWidth(), mStartPoint.y);
			canvas.drawRect(rect, mPaint);
		}
	}

	public void drawForTouchDown(PointF point) {
//		mPath.reset();
		
		mStartPoint = mEndPoint = point;
	}

	public void drawForTouchMove(PointF point) {
		mEndPoint = point;		
	}
	
	public void drawForTouchUp(PointF point) {
		mEndPoint = point;		
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

}
