package com.derek.ltapoc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.derek.ltapoc.view.model.LinesArea;

public class FormView extends View {
	private LinesArea mLinesArea;
	private Paint mBitmapPaint;
	private Paint mHorizontalTitlesPaint;
	private Paint mVerticalTitlesPaint;
	private Paint mTextsPaint;

	private int mHorizontalCount = 8;
	private int mVerticalCount = 7;
	private String[] mHorizontalTitles;
	private String[] mVerticalTitles;
	private String[][] mTexts;

	public FormView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setBackgroundColor(Color.WHITE);

		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		mHorizontalTitlesPaint = new Paint();
		mHorizontalTitlesPaint.setAntiAlias(true);
		mHorizontalTitlesPaint.setStyle(Paint.Style.FILL);
		mHorizontalTitlesPaint.setColor(Color.DKGRAY);
		mHorizontalTitlesPaint.setTextSize(25);

		mVerticalTitlesPaint = new Paint();
		mVerticalTitlesPaint.setAntiAlias(true);
		mVerticalTitlesPaint.setStyle(Paint.Style.FILL);
		mVerticalTitlesPaint.setColor(0xff91a1b8);
		mVerticalTitlesPaint.setTextSize(20);

		mTextsPaint = new Paint();
		mTextsPaint.setAntiAlias(true);
		mTextsPaint.setStyle(Paint.Style.FILL);
		mTextsPaint.setColor(0xff898a8a);
		mTextsPaint.setTextSize(15);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(mLinesArea.getBitmap(), mLinesArea.getRect().left, mLinesArea.getRect().top, mBitmapPaint);

		int linesAreaWidth = getWidth() / mHorizontalCount;
		for (int i = 0; i < mHorizontalTitles.length; i++) {
			String title = mHorizontalTitles[i];
			float width = mHorizontalTitlesPaint.measureText(title, 0, title.length());
			canvas.drawText(title, linesAreaWidth * i + (linesAreaWidth - width) / 2, 25 + 4, mHorizontalTitlesPaint);
		}

		int linesAreaHeight = getHeight() / mVerticalCount;
		for (int i = 0; i < mVerticalTitles.length; i++) {
			String title = mVerticalTitles[i];
			float width = mHorizontalTitlesPaint.measureText(title, 0, title.length());
			canvas.drawText(title, 0 + (linesAreaWidth - width) / 2, (i + 1) * linesAreaHeight + 20 + 5,
					mVerticalTitlesPaint);
		}

		for (int i = 0; i < mTexts.length; i++) {
			String[] verticalTexts = mTexts[i];
			for (int j = 0; j < verticalTexts.length; j++) {
				String text = verticalTexts[j];
				float width = mHorizontalTitlesPaint.measureText(text, 0, text.length());
				canvas.drawText(text, linesAreaWidth * (j + 1) + (linesAreaWidth - width) / 2, (i + 1)
						* linesAreaHeight + 15 + 10, mTextsPaint);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		int linesAreaWidth = w / mHorizontalCount;
		int linesAreaHeight = h / mVerticalCount;
		// int linesAreaHeight = (h - 100) / mVerticalCount;

		Rect rect = new Rect();
		rect.left = 0;
		rect.right = linesAreaWidth * mHorizontalCount;
		rect.top = 0;
		rect.bottom = linesAreaHeight * mVerticalCount;

		mLinesArea = new LinesArea(rect, mHorizontalCount, mVerticalCount);
		mLinesArea.drawLines();
	}

	public void setHorizontalCount(int horizontalCount) {
		this.mHorizontalCount = horizontalCount;
	}

	public void setVerticalCount(int verticalCount) {
		this.mVerticalCount = verticalCount;
	}

	public void setHorizontalTitles(String[] horizontalTitles) {
		mHorizontalTitles = horizontalTitles;
		mHorizontalCount = horizontalTitles.length;
	}

	public void setVerticalTitles(String[] verticalTitles) {
		mVerticalTitles = verticalTitles;
		mVerticalCount = verticalTitles.length + 1;
	}

	public void setTexts(String[][] texts) {
		mTexts = texts;
	}
}
