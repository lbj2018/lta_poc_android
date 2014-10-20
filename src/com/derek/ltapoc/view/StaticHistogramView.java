package com.derek.ltapoc.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.derek.ltapoc.model.LTADataStore;
import com.derek.ltapoc.view.model.HistogramArea;
import com.derek.ltapoc.view.model.HorizontalTextArea;
import com.derek.ltapoc.view.model.LinesArea;
import com.derek.ltapoc.view.model.VerticalTextArea;

public class StaticHistogramView extends View {
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

	public static final int PREV_SHOULDERING_RATE_COLOR = 0xFF1A4B8E;
	public static final int NEXT_SHOULDERING_RATE_COLOR = 0xFF2667D3;
	public static final int PREVIOUS_RATE__COLOR = 0xFF0584A6;
	public static final int SHOULDERING_RATE_VALUE_TEXT_COLOR = 0xFFFD9B4F;

	private Paint mBitmapPaint;
	private Paint mHistogramPaint;
	private Paint mPrevShoulderingRateHistogramPaint;
	private Paint mNextShoulderingRateHistogramPaint;
	private Paint mPreviousRateHistogramPaint;
	private Paint mHistogramValueTextPaint;
	private Paint mShoulderingRateHistogramValueTextPaint;
	private Paint mPreviousRateHistogramValueTextPaint;

	private float[] mHeightValues;
	private float[] mPreviousRateHeightValues;
	private float[] mPrevHeightValues;
	private float[] mNextHeightValues;

	private String[] mHeightValueTexts;
	private String[] mPreviousRateHeightValueTexts;
	private String[] mPrevHeightValueTexts;
	private String[] mNextHeightValueTexts;

	public StaticHistogramView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setBackgroundColor(Color.WHITE);
		mHistogramAreas = new ArrayList<HistogramArea>();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		mHistogramPaint = new Paint();
		mHistogramPaint.setColor(HistogramArea.HISTOGRAM_COLOR);
		mHistogramPaint.setStyle(Paint.Style.FILL);

		mPrevShoulderingRateHistogramPaint = new Paint();
		mPrevShoulderingRateHistogramPaint.setColor(PREV_SHOULDERING_RATE_COLOR);
		mPrevShoulderingRateHistogramPaint.setStyle(Paint.Style.FILL);

		mNextShoulderingRateHistogramPaint = new Paint();
		mNextShoulderingRateHistogramPaint.setColor(NEXT_SHOULDERING_RATE_COLOR);
		mNextShoulderingRateHistogramPaint.setStyle(Paint.Style.FILL);

		mPreviousRateHistogramPaint = new Paint();
		mPreviousRateHistogramPaint.setColor(PREVIOUS_RATE__COLOR);
		mPreviousRateHistogramPaint.setStyle(Paint.Style.FILL);

		mHistogramValueTextPaint = new Paint();
		mHistogramValueTextPaint.setAntiAlias(true);
		mHistogramValueTextPaint.setStyle(Paint.Style.FILL);
		mHistogramValueTextPaint.setColor(Color.DKGRAY);
		mHistogramValueTextPaint.setTextSize(25);

		mShoulderingRateHistogramValueTextPaint = new Paint();
		mShoulderingRateHistogramValueTextPaint.setAntiAlias(true);
		mShoulderingRateHistogramValueTextPaint.setStyle(Paint.Style.FILL);
		mShoulderingRateHistogramValueTextPaint.setColor(SHOULDERING_RATE_VALUE_TEXT_COLOR);
		mShoulderingRateHistogramValueTextPaint.setTextSize(20);

		mPreviousRateHistogramValueTextPaint = new Paint();
		mPreviousRateHistogramValueTextPaint.setAntiAlias(true);
		mPreviousRateHistogramValueTextPaint.setStyle(Paint.Style.FILL);
		mPreviousRateHistogramValueTextPaint.setColor(Color.WHITE);
		mPreviousRateHistogramValueTextPaint.setTextSize(25);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(mLinesArea.getBitmap(), mLinesArea.getRect().left, mLinesArea.getRect().top, mBitmapPaint);

		float prevRatio = LTADataStore.SHOULDERING_RATE_TIME_INTERVAL / (float) LTADataStore.TIME_INTERVAL;
		float nextRatio = 1 - (LTADataStore.SHOULDERING_RATE_TIME_INTERVAL / (float) LTADataStore.TIME_INTERVAL);
		for (int i = 0; i < mHeightValues.length; i++) {
			HistogramArea area = mHistogramAreas.get(i);
			int areaHeight = area.getRect().height();
			int areaWidth = area.getRect().width();

			float heightValue = mHeightValues[i];
			float previousTemplateHeightValue = mPreviousRateHeightValues[i];
			float prevHeightValue = mPrevHeightValues[i];
			float nextHeightValue = mNextHeightValues[i];

			if (prevHeightValue != 0) {
				canvas.drawRect(area.getRect().left + LinesArea.LINE_WIDTH, areaHeight - prevHeightValue * areaHeight,
						area.getRect().left + areaWidth * prevRatio, areaHeight, mPrevShoulderingRateHistogramPaint);

				if (nextHeightValue != 0) {
					canvas.drawRect(area.getRect().left + areaWidth * prevRatio, areaHeight - heightValue * areaHeight,
							area.getRect().left + areaWidth * nextRatio, areaHeight, mHistogramPaint);

					canvas.drawRect(area.getRect().left + areaWidth * nextRatio, areaHeight - nextHeightValue
							* areaHeight, area.getRect().left + areaWidth - LinesArea.LINE_WIDTH, areaHeight,
							mNextShoulderingRateHistogramPaint);
				} else {
					canvas.drawRect(area.getRect().left + areaWidth * prevRatio, areaHeight - heightValue * areaHeight,
							area.getRect().left + areaWidth - LinesArea.LINE_WIDTH, areaHeight, mHistogramPaint);
				}
			} else {
				if (nextHeightValue != 0) {
					canvas.drawRect(area.getRect().left + LinesArea.LINE_WIDTH, areaHeight - heightValue * areaHeight,
							area.getRect().left + areaWidth * nextRatio, areaHeight, mHistogramPaint);

					canvas.drawRect(area.getRect().left + areaWidth * nextRatio, areaHeight - nextHeightValue
							* areaHeight, area.getRect().left + areaWidth - LinesArea.LINE_WIDTH, areaHeight,
							mNextShoulderingRateHistogramPaint);
				} else {
					canvas.drawRect(area.getRect().left + LinesArea.LINE_WIDTH, areaHeight - heightValue * areaHeight,
							area.getRect().left + areaWidth - LinesArea.LINE_WIDTH, areaHeight, mHistogramPaint);
				}
			}

			// Draw Previous Rate Template Height Value Rectangle
			if (previousTemplateHeightValue != 0) {
				canvas.drawRect(area.getRect().left + LinesArea.LINE_WIDTH, areaHeight - previousTemplateHeightValue
						* areaHeight, area.getRect().left + areaWidth - LinesArea.LINE_WIDTH, areaHeight
						- previousTemplateHeightValue * areaHeight + 30, mPreviousRateHistogramPaint);
			}
		}

		for (int i = 0; i < mHeightValues.length; i++) {
			HistogramArea area = mHistogramAreas.get(i);
			int areaHeight = area.getRect().height();
			int areaWidth = area.getRect().width();

			float heightValue = mHeightValues[i];
			float previousTemplateHeightValue = mPreviousRateHeightValues[i];
			float prevHeightValue = mPrevHeightValues[i];
			float nextHeightValue = mNextHeightValues[i];

			String heightValueText = mHeightValueTexts[i];
			String previousTemplateHeightValueText = mPreviousRateHeightValueTexts[i];
			String prevHeightValueText = mPrevHeightValueTexts[i];
			String nextHeightValueText = mNextHeightValueTexts[i];

			float textWidth = 0;
			float shoulderingRateWidth = prevRatio * areaWidth;

			if (heightValue > 0) {
				if (prevHeightValue != 0) {
					// draw prev value text
					textWidth = mShoulderingRateHistogramValueTextPaint.measureText(prevHeightValueText, 0,
							prevHeightValueText.length());

					canvas.drawText(prevHeightValueText, area.getRect().left + (shoulderingRateWidth - textWidth)
							/ 2.0f, areaHeight - prevHeightValue * areaHeight - 5,
							mShoulderingRateHistogramValueTextPaint);

					if (nextHeightValue != 0) {
						// draw current value text
						textWidth = mHistogramValueTextPaint.measureText(heightValueText, 0, heightValueText.length());

						canvas.drawText(heightValueText, area.getRect().left + areaWidth * prevRatio
								+ (areaWidth - 2 * shoulderingRateWidth - textWidth) / 2, areaHeight - heightValue
								* areaHeight + 25 + 5, mHistogramValueTextPaint);

						// draw next value text
						textWidth = mShoulderingRateHistogramValueTextPaint.measureText(nextHeightValueText, 0,
								nextHeightValueText.length());

						canvas.drawText(nextHeightValueText, area.getRect().left + areaWidth * nextRatio
								+ (shoulderingRateWidth - textWidth) / 2,
								areaHeight - nextHeightValue * areaHeight - 5, mShoulderingRateHistogramValueTextPaint);
					} else {
						// draw current value text
						textWidth = mHistogramValueTextPaint.measureText(heightValueText, 0, heightValueText.length());

						canvas.drawText(heightValueText, area.getRect().left + areaWidth * prevRatio
								+ (areaWidth - shoulderingRateWidth - textWidth) / 2, areaHeight - heightValue
								* areaHeight + 25 + 5, mHistogramValueTextPaint);
					}
				} else {
					if (nextHeightValue != 0) {
						// draw current value text
						textWidth = mHistogramValueTextPaint.measureText(heightValueText, 0, heightValueText.length());

						canvas.drawText(heightValueText, area.getRect().left
								+ (areaWidth - shoulderingRateWidth - textWidth) / 2, areaHeight - heightValue
								* areaHeight + 25 + 5, mHistogramValueTextPaint);

						// draw next value text
						textWidth = mShoulderingRateHistogramValueTextPaint.measureText(nextHeightValueText, 0,
								nextHeightValueText.length());

						canvas.drawText(nextHeightValueText, area.getRect().left + areaWidth * nextRatio
								+ (shoulderingRateWidth - textWidth) / 2,
								areaHeight - nextHeightValue * areaHeight - 5, mShoulderingRateHistogramValueTextPaint);
					} else {
						// draw current value text
						textWidth = mHistogramValueTextPaint.measureText(heightValueText, 0, heightValueText.length());

						canvas.drawText(heightValueText, area.getRect().left + (areaWidth - textWidth) / 2, areaHeight
								- heightValue * areaHeight + 25 + 5, mHistogramValueTextPaint);
					}
				}
			}

			// Draw Previous Rate Template Height Value Text
			if (previousTemplateHeightValue != 0) {
				textWidth = mPreviousRateHistogramValueTextPaint.measureText(previousTemplateHeightValueText, 0,
						previousTemplateHeightValueText.length());
				canvas.drawText(previousTemplateHeightValueText, area.getRect().left + (areaWidth - textWidth) / 2,
						areaHeight - previousTemplateHeightValue * areaHeight + 25,
						mPreviousRateHistogramValueTextPaint);
			}
		}

		canvas.drawBitmap(mHorizontalTextArea.getBitmap(), mHorizontalTextArea.getRect().left,
				mHorizontalTextArea.getRect().top, mBitmapPaint);
		canvas.drawBitmap(mVerticalTextArea.getBitmap(), mVerticalTextArea.getRect().left,
				mVerticalTextArea.getRect().top, mBitmapPaint);
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

		for (int i = 0; i < mHorizontalCount; i++) {
			Rect histogramAreaRect = new Rect();
			histogramAreaRect.left = Y_AXIS_VALUE_DISTANCE + i * linesAreaWidth;
			histogramAreaRect.right = Y_AXIS_VALUE_DISTANCE + (i + 1) * linesAreaWidth;
			histogramAreaRect.top = 0;
			histogramAreaRect.bottom = linesAreaHeight * mVerticalCount;

			HistogramArea area = new HistogramArea(histogramAreaRect, mVerticalCount);
			mHistogramAreas.add(area);
		}

		Rect horizontalTextAreaRect = new Rect(0, h - X_AXIS_VALUE_DISTANCE, w, h);
		mHorizontalTextArea = new HorizontalTextArea(horizontalTextAreaRect, mHorizontalTexts);
		mHorizontalTextArea.drawTexts();

		Rect verticalTextAreaRect = new Rect(0, 0, Y_AXIS_VALUE_DISTANCE, h);
		mVerticalTextArea = new VerticalTextArea(verticalTextAreaRect, mVerticalTexts);
		mVerticalTextArea.drawTexts();
	}

	/*
	 * Getter and Setter Methods
	 */
	public void setHeightValues(float[] heightValues) {
		mHeightValues = heightValues;
	}

	public void setPrevHeightValues(float[] prevHeightValues) {
		mPrevHeightValues = prevHeightValues;
	}

	public void setNextHeightValues(float[] nextHeightValues) {
		mNextHeightValues = nextHeightValues;
	}

	public void setHeightValueTexts(String[] heightValueTexts) {
		mHeightValueTexts = heightValueTexts;
	}

	public void setPrevHeightValueTexts(String[] prevHeightValueTexts) {
		mPrevHeightValueTexts = prevHeightValueTexts;
	}

	public void setNextHeightValueTexts(String[] nextHeightValueTexts) {
		mNextHeightValueTexts = nextHeightValueTexts;
	}

	public float[] getPreviousRateHeightValues() {
		return mPreviousRateHeightValues;
	}

	public void setPreviousRateHeightValues(float[] previousRateHeightValues) {
		mPreviousRateHeightValues = previousRateHeightValues;
	}

	public String[] getPreviousRateHeightValueTexts() {
		return mPreviousRateHeightValueTexts;
	}

	public void setPreviousRateHeightValueTexts(String[] previousRateHeightValueTexts) {
		mPreviousRateHeightValueTexts = previousRateHeightValueTexts;
	}

	public void setHorizontalTexts(String[] horizontalTexts) {
		if (horizontalTexts != null && horizontalTexts.length > 0) {
			mHorizontalTexts = horizontalTexts;
			mHorizontalCount = horizontalTexts.length - 1;

			mHeightValues = new float[mHorizontalCount];
			mPreviousRateHeightValues = new float[mHorizontalCount];
			mPrevHeightValues = new float[mHorizontalCount];
			mNextHeightValues = new float[mHorizontalCount];
		}
	}

	public void setVerticalTexts(String[] verticalTexts) {
		if (verticalTexts != null) {
			mVerticalTexts = verticalTexts;
			mVerticalCount = verticalTexts.length;
		}
	}
}
