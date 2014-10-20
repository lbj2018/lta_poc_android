package com.derek.ltapoc.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LTADataStore {
	private static LTADataStore sLTADataStore;

	private ArrayList<RateTemplate> mRateTemplates;

	public static final int TIME_INTERVAL = 30; // for minutes
	public static final int SHOULDERING_RATE_TIME_INTERVAL = 5; // for minutes
	public static final int HORIZONTAL_COUNT = 11;
	public static final float PRINCE_INTERVAL = 1;
	public static final int VERTICAL_COUNT = 6;
	private Date mStartTime;

	public ArrayList<RateTemplate> getRateTemplates() {
		return mRateTemplates;
	}

	public void setRateTemplates(ArrayList<RateTemplate> rateTemplates) {
		mRateTemplates = rateTemplates;
	}

	private LTADataStore() {
		mRateTemplates = new ArrayList<RateTemplate>();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.MINUTE, 30);
		mStartTime = calendar.getTime();
	}

	public static LTADataStore get() {
		if (sLTADataStore == null) {
			sLTADataStore = new LTADataStore();
		}
		return sLTADataStore;
	}

	public RateTemplate getRateTemplate(String rateTemplateId) {
		RateTemplate rateTemplate = null;

		if (rateTemplateId != null) {
			for (RateTemplate aTemplate : mRateTemplates) {
				if (rateTemplateId.equals(aTemplate.getRateTemplateId())) {
					rateTemplate = aTemplate;
					break;
				}
			}
		}

		return rateTemplate;
	}

	public RateTemplate getPreviousRateTemplate(String rateTemplateId) {
		RateTemplate previousRateTemplate = null;

		RateTemplate rateTemplate = getRateTemplate(rateTemplateId);

		if (rateTemplate != null) {
			int index = mRateTemplates.indexOf(rateTemplate);
			if (index >= 0 && index < mRateTemplates.size() - 1) {
				previousRateTemplate = mRateTemplates.get(index + 1);
			}
		}

		return previousRateTemplate;
	}

	public String getTimeString(int timeInterval) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mStartTime);
		calendar.add(Calendar.MINUTE, timeInterval);
		Date newDate = calendar.getTime();
		String timeString = DateFormatUtil.getDateStringFromDate(newDate, "HH:mm");
		return timeString;
	}

	public String getPriceString(float heightValue) {
		String priceString = String.format(Locale.ENGLISH, "$%.2f", heightValue * PRINCE_INTERVAL);
		return priceString;
	}

	public static String[] getHorizontalTexts() {
		String[] horizontalTexts = new String[LTADataStore.HORIZONTAL_COUNT];

		for (int i = 0; i < LTADataStore.HORIZONTAL_COUNT; i++) {
			String text = LTADataStore.get().getTimeString(LTADataStore.TIME_INTERVAL * i);
			horizontalTexts[i] = text;
		}

		return horizontalTexts;
	}

	public static String[] getVerticalTexts() {
		String[] verticalTexts = new String[LTADataStore.VERTICAL_COUNT];

		for (int i = 0; i < LTADataStore.VERTICAL_COUNT; i++) {
			String text = LTADataStore.get().getPriceString((i + 1));
			verticalTexts[i] = text;
		}

		return verticalTexts;
	}

	public static float prevShoudleringRate(int value, int prevValue) {
		return prevValue + (value - prevValue) / 2.0f;
	}

	public static float nextShoudleringRate(int value, int nextValue) {
		return value - (value - nextValue) / 2.0f;
	}
}
