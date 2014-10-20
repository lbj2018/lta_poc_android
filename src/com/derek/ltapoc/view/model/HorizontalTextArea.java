package com.derek.ltapoc.view.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class HorizontalTextArea {
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;

	private Rect mRect;
	private String[] mTexts;

	public static final int TEXT_SIZE = 20;

	public HorizontalTextArea(Rect rect, String[] texts) {
		mRect = rect;
		mTexts = texts;

		mBitmap = Bitmap.createBitmap(mRect.width(), mRect.height(), Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.DKGRAY);
		mPaint.setTextSize(TEXT_SIZE);
	}

	public void drawTexts() {

		int linesAreaWidth = (mRect.width() - 100) / (mTexts.length - 1);

		for (int i = 0; i < mTexts.length; i++) {
			String text = mTexts[i];
			mCanvas.drawText(text, 100 + i * linesAreaWidth, TEXT_SIZE, mPaint);
		}
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public Rect getRect() {
		return mRect;
	}
}
