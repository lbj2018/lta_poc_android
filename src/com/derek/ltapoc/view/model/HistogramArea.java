package com.derek.ltapoc.view.model;

import android.graphics.Path;
import android.graphics.Rect;

public class HistogramArea {
	private Path mPath;
	private Rect mRect;
	private int mHeightValue;
	private int mVerticalCount;
	public static final int HISTOGRAM_COLOR = 0xFF73AFBF;

	public HistogramArea(Rect rect, int verticalCount) {
		mRect = rect;
		mVerticalCount = verticalCount;
		mPath = new Path();
	}

	private void drawHistogram() {
		int width = mRect.width();
		int height = mRect.height();

		int linesAreaHeight = height / mVerticalCount;

		mPath.reset();
		mPath.addRect(mRect.left + LinesArea.LINE_WIDTH, height - mHeightValue * linesAreaHeight, mRect.left + width
				- LinesArea.LINE_WIDTH, height, Path.Direction.CCW);
	}

	public int getHeightValue() {
		return mHeightValue;
	}

	public void setHeightValue(int heightValue) {
		if (heightValue > mVerticalCount || heightValue < 0)
			return;

		mHeightValue = heightValue;
		drawHistogram();
	}

	public Path getPath() {
		return mPath;
	}

	public Rect getRect() {
		return mRect;
	}
}
