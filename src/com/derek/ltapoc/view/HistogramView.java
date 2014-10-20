package com.derek.ltapoc.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.derek.ltapoc.view.model.HistogramArea;
import com.derek.ltapoc.view.model.HorizontalTextArea;
import com.derek.ltapoc.view.model.LinesArea;
import com.derek.ltapoc.view.model.VerticalTextArea;

public class HistogramView extends View {
	private LinesArea mLinesArea;
	private HorizontalTextArea mHorizontalTextArea;
	private VerticalTextArea mVerticalTextArea;
	private ArrayList<HistogramArea> mHistogramAreas;
	public int mHorizontalCount = 10;
	public int mVerticalCount = 6;
	private String[] mHorizontalTexts;
	private String[] mVerticalTexts;
	public static final int Y_AXIS_VALUE_DISTANCE = 100;
	public static final int X_AXIS_VALUE_DISTANCE = 60;

	private Paint mBitmapPaint;
	private Paint mHistogramPaint;
	private Paint mHistogramValueTextPaint;
	private PointF mStartPoint;
	private PointF mHorizontalMoveStart;

	private boolean mCanDraw = true;
	private int[] mHeightValues;

	public int[] getHeightValues() {
		return mHeightValues;
	}

	public void setHeightValues(int[] heightValues) {
		mHeightValues = heightValues;

		if (mHistogramAreas.size() > 0) {
			for (int i = 0; i < mHistogramAreas.size(); i++) {
				HistogramArea area = mHistogramAreas.get(i);
				area.setHeightValue(mHeightValues[i]);
			}
		}

		invalidate();
	}

	public boolean isCanDraw() {
		return mCanDraw;
	}

	public void setCanDraw(boolean canDraw) {
		mCanDraw = canDraw;
	}

	public void setHorizontalTexts(String[] horizontalTexts) {
		if (horizontalTexts != null && horizontalTexts.length > 0) {
			mHorizontalTexts = horizontalTexts;
			mHorizontalCount = horizontalTexts.length - 1;
		}
	}

	public void setVerticalTexts(String[] verticalTexts) {
		if (verticalTexts != null) {
			mVerticalTexts = verticalTexts;
			mVerticalCount = verticalTexts.length;
		}
	}

	public HistogramView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setBackgroundColor(Color.WHITE);
		mHistogramAreas = new ArrayList<HistogramArea>();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		mHistogramPaint = new Paint();
		mHistogramPaint.setColor(HistogramArea.HISTOGRAM_COLOR);
		mHistogramPaint.setStyle(Paint.Style.FILL);

		mHistogramValueTextPaint = new Paint();
		mHistogramValueTextPaint.setAntiAlias(true);
		mHistogramValueTextPaint.setStyle(Paint.Style.FILL);
		mHistogramValueTextPaint.setColor(Color.DKGRAY);
		mHistogramValueTextPaint.setTextSize(25);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(mLinesArea.getBitmap(), mLinesArea.getRect().left, mLinesArea.getRect().top, mBitmapPaint);

		for (int i = 0; i < mHistogramAreas.size(); i++) {
			HistogramArea area = mHistogramAreas.get(i);
			int heightValue = mHeightValues[i];

			canvas.drawPath(area.getPath(), mHistogramPaint);

			// draw height value text
			if (heightValue > 0) {
				String text = mVerticalTexts[heightValue - 1];
				float textWidth = mHistogramValueTextPaint.measureText(text, 0, text.length());
				int linesAreaHeight = area.getRect().height() / mVerticalCount;
				canvas.drawText(text, area.getRect().left + (area.getRect().width() - textWidth) / 2, area.getRect()
						.height() - heightValue * linesAreaHeight + 25 + 5, mHistogramValueTextPaint);
			}
		}

		canvas.drawBitmap(mHorizontalTextArea.getBitmap(), mHorizontalTextArea.getRect().left,
				mHorizontalTextArea.getRect().top, mBitmapPaint);
		canvas.drawBitmap(mVerticalTextArea.getBitmap(), mVerticalTextArea.getRect().left,
				mVerticalTextArea.getRect().top, mBitmapPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!this.mCanDraw)
			return false;

		float x = event.getX();
		float y = event.getY();

		PointF point = new PointF(x, y);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStartPoint = point;
			mHorizontalMoveStart = point;
			break;
		case MotionEvent.ACTION_MOVE:
			drawHistogram(point);
			break;
		case MotionEvent.ACTION_UP:
			drawHistogram(point);
			break;
		default:
			return false;
		}

		return true;
	}

	private void drawHistogram(PointF point) {

		// Vertical move
		HistogramArea histogramArea = null;
		for (HistogramArea area : mHistogramAreas) {
			Rect rect = area.getRect();
			if (rect.contains((int) mStartPoint.x, (int) mStartPoint.y) && rect.contains((int) point.x, (int) point.y)) {
				histogramArea = area;
				break;
			}
		}

		if (histogramArea != null) {
			float verticalDistance = mStartPoint.y - point.y;

			int heightValue = ((int) verticalDistance) / ((histogramArea.getRect().height() / mVerticalCount) * 3 / 4);

			if (heightValue != 0) {
				mStartPoint = point;

				int newHeightValue = histogramArea.getHeightValue() + heightValue;
				histogramArea.setHeightValue(newHeightValue);

				invalidate();
			}
		}

		// Horizontal move
		HistogramArea horizontalMoveStartArea = null;
		int horizontalMoveStartIndex = 0;
		for (int i = 0; i < mHistogramAreas.size(); i++) {
			HistogramArea area = mHistogramAreas.get(i);
			Rect rect = area.getRect();
			if (rect.contains((int) mHorizontalMoveStart.x, (int) mHorizontalMoveStart.y)) {
				horizontalMoveStartArea = area;
				horizontalMoveStartIndex = i;
				break;
			}
		}

		Log.i("HistogramView", "Start horizontal move");
		if (horizontalMoveStartArea != null) {
			float horizontalMoveDistance = point.x - mHorizontalMoveStart.x;
			int oneWidthValue = (getWidth() - Y_AXIS_VALUE_DISTANCE) / mHorizontalCount;
			int horizontalMoveWidthValue = ((int) horizontalMoveDistance) / (oneWidthValue * 3 / 4);

			int from, to;
			if (horizontalMoveDistance > 0) {
				from = horizontalMoveStartIndex;
				to = mHistogramAreas.size() < (from + horizontalMoveWidthValue) ? mHistogramAreas.size()
						: (from + horizontalMoveWidthValue);
			} else {
				from = (horizontalMoveStartIndex + horizontalMoveWidthValue) >= 0 ? (horizontalMoveStartIndex + horizontalMoveWidthValue)
						: 0;
				to = horizontalMoveStartIndex;
			}
			Log.i("HistogramView", "horizontalMoveDistance: " + horizontalMoveDistance);
			Log.i("HistogramView", "oneWidthValue: " + oneWidthValue);
			Log.i("HistogramView", "horizontalMoveWidthValue: " + horizontalMoveWidthValue);
			Log.i("HistogramView", "from: " + from);
			Log.i("HistogramView", "to: " + to);

			for (int i = from; i < to; i++) {
				HistogramArea area = mHistogramAreas.get(i);
				area.setHeightValue(horizontalMoveStartArea.getHeightValue());
			}
			invalidate();
		}

		for (int i = 0; i < mHistogramAreas.size(); i++) {
			HistogramArea area = mHistogramAreas.get(i);
			mHeightValues[i] = area.getHeightValue();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		int linesAreaWidth = (w - Y_AXIS_VALUE_DISTANCE) / mHorizontalCount;
		int linesAreaHeight = (h - X_AXIS_VALUE_DISTANCE) / mVerticalCount;

		Rect rect = new Rect();
		rect.left = Y_AXIS_VALUE_DISTANCE;
		rect.right = Y_AXIS_VALUE_DISTANCE + linesAreaWidth * mHorizontalCount;
		rect.top = 0;
		rect.bottom = linesAreaHeight * mVerticalCount;

		mLinesArea = new LinesArea(rect, mHorizontalCount, mVerticalCount);
		mLinesArea.drawLines();

		if (mHeightValues == null) {
			mHeightValues = new int[mHorizontalCount];
			for (int i = 0; i < mHorizontalCount; i++) {
				mHeightValues[i] = 0;
			}
		}
		for (int i = 0; i < mHorizontalCount; i++) {
			Rect histogramAreaRect = new Rect();
			histogramAreaRect.left = Y_AXIS_VALUE_DISTANCE + i * linesAreaWidth;
			histogramAreaRect.right = Y_AXIS_VALUE_DISTANCE + (i + 1) * linesAreaWidth;
			histogramAreaRect.top = 0;
			histogramAreaRect.bottom = linesAreaHeight * mVerticalCount;

			HistogramArea area = new HistogramArea(histogramAreaRect, mVerticalCount);
			area.setHeightValue(mHeightValues[i]);
			mHistogramAreas.add(area);
		}

		Rect horizontalTextAreaRect = new Rect(0, h - X_AXIS_VALUE_DISTANCE, w, h);
		mHorizontalTextArea = new HorizontalTextArea(horizontalTextAreaRect, mHorizontalTexts);
		mHorizontalTextArea.drawTexts();

		Rect verticalTextAreaRect = new Rect(0, 0, Y_AXIS_VALUE_DISTANCE, h);
		mVerticalTextArea = new VerticalTextArea(verticalTextAreaRect, mVerticalTexts);
		mVerticalTextArea.drawTexts();
	}
}
