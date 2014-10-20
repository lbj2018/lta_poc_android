package com.derek.ltapoc;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.derek.ltapoc.model.ChargingRate;
import com.derek.ltapoc.model.ChargingRecord;
import com.derek.ltapoc.model.DetailedChargeTableUtil;
import com.derek.ltapoc.model.LTADataStore;
import com.derek.ltapoc.model.RateTemplate;
import com.derek.ltapoc.view.FormView;
import com.derek.ltapoc.view.StaticHistogramView;

public class ReviewChangesFragment extends Fragment implements View.OnClickListener {
	private TextView mSubmitTextView;
	private StaticHistogramView mWeekdaysStaticHistogramView;
	private StaticHistogramView mSaturStaticHistogramView;
	private TextView mViewCurrentChargeRateTextView;
	private TextView mViewShoulderingRateTextView;
	private TextView mViewPreviousChargeRateTextView;
	private FormView mFormView;
	private String[] mHorizontalTitles = { "Day Type", "Start Time", "End Time", "Cars", "Motocycles", "Taxi", "Bus",
			"Lorry" };

	private String mRateTemplateId;
	private ChargingRate mChargingRate;
	private ReviewChangesFragmentCallbacks mCallbacks;
	private int mSelectedTextViewIndex; // 0 for view current charge rate, 1 for
										// view shouldering rate, 2 for view
										// previous rate

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_review_changes, container, false);

		mSubmitTextView = (TextView) rootView.findViewById(R.id.text_view_review_changes_submit);
		mWeekdaysStaticHistogramView = (StaticHistogramView) rootView
				.findViewById(R.id.view_static_histogram_view_weekdays);
		mSaturStaticHistogramView = (StaticHistogramView) rootView
				.findViewById(R.id.view_static_histogram_view_saturday);
		mViewCurrentChargeRateTextView = (TextView) rootView.findViewById(R.id.text_view_view_current_charge_rate);
		mViewShoulderingRateTextView = (TextView) rootView.findViewById(R.id.text_view_view_shouldering_rate);
		mViewPreviousChargeRateTextView = (TextView) rootView.findViewById(R.id.text_view_view_previous_charge_rate);
		mFormView = (FormView) rootView.findViewById(R.id.view_review_changes_form);

		mViewCurrentChargeRateTextView.setOnClickListener(this);
		mViewShoulderingRateTextView.setOnClickListener(this);
		mViewPreviousChargeRateTextView.setOnClickListener(this);

		mWeekdaysStaticHistogramView.setHorizontalTexts(LTADataStore.getHorizontalTexts());
		mWeekdaysStaticHistogramView.setVerticalTexts(LTADataStore.getVerticalTexts());
		mSaturStaticHistogramView.setHorizontalTexts(LTADataStore.getHorizontalTexts());
		mSaturStaticHistogramView.setVerticalTexts(LTADataStore.getVerticalTexts());

		mFormView.setHorizontalTitles(mHorizontalTitles);

		boolean applyShoulderingRate = mChargingRate.isApplyShoulderingRate();
		int recordsCounts = DetailedChargeTableUtil.getCountOfRecords(mChargingRate, applyShoulderingRate);
		String[] verticalTitles = DetailedChargeTableUtil.getVerticalTitles(mChargingRate, applyShoulderingRate);
		String[][] texts = DetailedChargeTableUtil.getPriceValueTexts(mChargingRate, mHorizontalTitles.length,
				applyShoulderingRate);

		LayoutParams params = mFormView.getLayoutParams();
		params.height = (recordsCounts + 1) * 40;
		LinearLayout linearLayout = (LinearLayout) mFormView.getParent();
		linearLayout.invalidate();

		mFormView.setVerticalTitles(verticalTitles);
		mFormView.setTexts(texts);

		mSubmitTextView.setBackgroundColor(0xffff8917);
		mSubmitTextView.setTextColor(Color.WHITE);

		mSubmitTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallbacks.submitRateTemplate();
			}
		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (ReviewChangesFragmentCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onResume() {
		super.onResume();

		updateUI();
	}

	private void updateUI() {
		updateTextViews();

		RateTemplate previousTemplate = LTADataStore.get().getPreviousRateTemplate(mRateTemplateId);

		if (previousTemplate != null) {
			this.reloadStatisHistogramView(mWeekdaysStaticHistogramView, mChargingRate.getWeekdayChargingRecords(),
					previousTemplate.getChargingRate().getWeekdayChargingRecords());
			this.reloadStatisHistogramView(mSaturStaticHistogramView, mChargingRate.getSaturdayChargingRecords(),
					previousTemplate.getChargingRate().getSaturdayChargingRecords());
		} else {
			this.reloadStatisHistogramView(mWeekdaysStaticHistogramView, mChargingRate.getWeekdayChargingRecords(),
					null);
			this.reloadStatisHistogramView(mSaturStaticHistogramView, mChargingRate.getSaturdayChargingRecords(), null);
		}
	}

	private void reloadStatisHistogramView(StaticHistogramView statisHistogramView, ChargingRecord[] records,
			ChargingRecord[] previousTemplateRecords) {

		boolean applyShoulderingRate = mChargingRate.isApplyShoulderingRate();

		float[] heightValues = new float[LTADataStore.HORIZONTAL_COUNT - 1];
		float[] previousTemplateHeightValues = new float[LTADataStore.HORIZONTAL_COUNT - 1];
		float[] prevHeightValues = new float[LTADataStore.HORIZONTAL_COUNT - 1];
		float[] nextHeightValues = new float[LTADataStore.HORIZONTAL_COUNT - 1];
		String[] heightValueTexts = new String[LTADataStore.HORIZONTAL_COUNT - 1];
		String[] previousTampletHeightValueTexts = new String[LTADataStore.HORIZONTAL_COUNT - 1];
		String[] prevHeightValueTexts = new String[LTADataStore.HORIZONTAL_COUNT - 1];
		String[] nextHeightValueTexts = new String[LTADataStore.HORIZONTAL_COUNT - 1];

		for (int i = 0; i < records.length; i++) {
			ChargingRecord record = records[i];

			if (applyShoulderingRate && mSelectedTextViewIndex == 1) {
				if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS
						|| record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS_NEXT) {
					float prevShoulderingRate = LTADataStore.prevShoudleringRate(record.getValue(),
							record.getPrevRecordValue());
					float prevHeightValue = prevShoulderingRate / (float) LTADataStore.VERTICAL_COUNT;
					prevHeightValues[i] = prevHeightValue;

					prevHeightValueTexts[i] = LTADataStore.get().getPriceString(prevShoulderingRate);
				}
				if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NEXT
						|| record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS_NEXT) {
					float nextShoulderingRate = LTADataStore.prevShoudleringRate(record.getValue(),
							record.getNextRecordValue());
					float nextHeightValue = nextShoulderingRate / (float) LTADataStore.VERTICAL_COUNT;
					nextHeightValues[i] = nextHeightValue;

					nextHeightValueTexts[i] = LTADataStore.get().getPriceString(nextShoulderingRate);
				}
			}

			heightValues[i] = record.getValue() / (float) LTADataStore.VERTICAL_COUNT;

			heightValueTexts[i] = LTADataStore.get().getPriceString(record.getValue());

			if (mSelectedTextViewIndex == 2) {
				if (previousTemplateRecords != null) {
					ChargingRecord prevTemplateRecord = previousTemplateRecords[i];
					previousTemplateHeightValues[i] = prevTemplateRecord.getValue()
							/ (float) LTADataStore.VERTICAL_COUNT;
					previousTampletHeightValueTexts[i] = LTADataStore.get().getPriceString(
							prevTemplateRecord.getValue());
				}
			}
		}

		statisHistogramView.setHeightValues(heightValues);
		statisHistogramView.setPrevHeightValues(prevHeightValues);
		statisHistogramView.setNextHeightValues(nextHeightValues);
		statisHistogramView.setHeightValueTexts(heightValueTexts);
		statisHistogramView.setPrevHeightValueTexts(prevHeightValueTexts);
		statisHistogramView.setNextHeightValueTexts(nextHeightValueTexts);
		statisHistogramView.setPreviousRateHeightValues(previousTemplateHeightValues);
		statisHistogramView.setPreviousRateHeightValueTexts(previousTampletHeightValueTexts);
		statisHistogramView.invalidate();
	}

	private void updateTextViews() {
		if (mSelectedTextViewIndex == 0) {
			mViewCurrentChargeRateTextView.setTextColor(0xff85b8c7);
			mViewShoulderingRateTextView.setTextColor(Color.DKGRAY);
			mViewPreviousChargeRateTextView.setTextColor(Color.DKGRAY);

			mViewCurrentChargeRateTextView.setBackgroundColor(Color.WHITE);
			mViewShoulderingRateTextView.setBackgroundColor(Color.LTGRAY);
			mViewPreviousChargeRateTextView.setBackgroundColor(Color.LTGRAY);
		} else if (mSelectedTextViewIndex == 1) {
			mViewCurrentChargeRateTextView.setTextColor(Color.DKGRAY);
			mViewShoulderingRateTextView.setTextColor(0xff85b8c7);
			mViewPreviousChargeRateTextView.setTextColor(Color.DKGRAY);

			mViewCurrentChargeRateTextView.setBackgroundColor(Color.LTGRAY);
			mViewShoulderingRateTextView.setBackgroundColor(Color.WHITE);
			mViewPreviousChargeRateTextView.setBackgroundColor(Color.LTGRAY);
		} else if (mSelectedTextViewIndex == 2) {
			mViewCurrentChargeRateTextView.setTextColor(Color.DKGRAY);
			mViewShoulderingRateTextView.setTextColor(Color.DKGRAY);
			mViewPreviousChargeRateTextView.setTextColor(0xff85b8c7);

			mViewCurrentChargeRateTextView.setBackgroundColor(Color.LTGRAY);
			mViewShoulderingRateTextView.setBackgroundColor(Color.LTGRAY);
			mViewPreviousChargeRateTextView.setBackgroundColor(Color.WHITE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			TextView textView = (TextView) v;
			if (textView == mViewCurrentChargeRateTextView) {
				mSelectedTextViewIndex = 0;
			} else if (textView == mViewShoulderingRateTextView) {
				mSelectedTextViewIndex = 1;
			} else if (textView == mViewPreviousChargeRateTextView) {
				mSelectedTextViewIndex = 2;
			}
			updateUI();
		}
	}

	/*
	 * Getter and Setter Methods
	 */
	public void setRateTemplateId(String rateTemplateId) {
		mRateTemplateId = rateTemplateId;
	}

	public void setChargingRate(ChargingRate chargingRate) {
		mChargingRate = chargingRate;
	}

	/*
	 * Interface
	 */
	public interface ReviewChangesFragmentCallbacks {
		void submitRateTemplate();
	}
}
