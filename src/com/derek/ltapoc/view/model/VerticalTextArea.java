package com.derek.ltapoc.view.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class VerticalTextArea {
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;

	private Rect mRect;
	private final String[] mTexts;

	public static final int TEXT_SIZE = 20;

	public VerticalTextArea(Rect rect, String[] texts) {
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

		int linesAreaHeight = (mRect.height() - 100) / mTexts.length;

		for (int i = mTexts.length - 1; i >= 0; i--) {
			String text = mTexts[i];
			mCanvas.drawText(text, 15, (mTexts.length - 1 - i) * linesAreaHeight + TEXT_SIZE, mPaint);
		}
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public Rect getRect() {
		return mRect;
	}
}
