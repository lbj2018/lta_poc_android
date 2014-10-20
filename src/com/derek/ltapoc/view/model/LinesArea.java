package com.derek.ltapoc.view.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;

public class LinesArea {
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Path mPath;
	private Paint mPaint;

	private Rect mRect;
	private int mHorizontalCount;
	private int mVerticalCount;
	public static final int LINE_WIDTH = 2;

	public LinesArea(Rect rect, int horizontalCount, int verticalCount) {
		mRect = rect;
		mHorizontalCount = horizontalCount;
		mVerticalCount = verticalCount;

		mBitmap = Bitmap.createBitmap(mRect.width(), mRect.height(), Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

		mPath = new Path();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.LTGRAY);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(LINE_WIDTH);
	}

	public void drawLines() {
		int width = mRect.width();
		int height = mRect.height();

		float length = width / mHorizontalCount;

		// vertical lines
		for (int i = 0; i < mHorizontalCount + 1; i++) {
			PointF start = new PointF(i * length, 0);
			PointF end = new PointF(i * length, mRect.height());

			mPath.moveTo(start.x, start.y);
			mPath.lineTo(end.x, end.y);
		}

		// horizontal lines
		length = height / mVerticalCount;
		for (int i = 0; i < mVerticalCount + 1; i++) {
			PointF start = new PointF(0, i * length);
			PointF end = new PointF(mRect.width(), i * length);

			mPath.moveTo(start.x, start.y);
			mPath.lineTo(end.x, end.y);
		}

		mCanvas.drawPath(mPath, mPaint);
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public Rect getRect() {
		return mRect;
	}
}
