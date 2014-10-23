package com.derek.ltapoc;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.derek.ltapoc.model.ChargingRate;
import com.derek.ltapoc.model.ChargingRecord;
import com.derek.ltapoc.model.LTADataStore;
import com.derek.ltapoc.model.RateTemplate;
import com.derek.ltapoc.view.HistogramView;

public class ChargingRateFragment extends Fragment {
	private HistogramView mHistogramView;
	private CheckBox mApplyShoulderingRateCheckBox;
	private CheckBox mViewPreviousChargeRateCheckBox;

	private boolean mIsWeekday;
	private String mRateTemplateId;
	private ChargingRate mChargingRate;

	public ChargingRateFragment() {
		mIsWeekday = true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_charging_rate, container, false);

		mHistogramView = (HistogramView) rootView.findViewById(R.id.view_charging_rate_histogram);
		mApplyShoulderingRateCheckBox = (CheckBox) rootView
				.findViewById(R.id.check_box_charging_rate_apply_shouldering_rate);
		mViewPreviousChargeRateCheckBox = (CheckBox) rootView
				.findViewById(R.id.check_box_charging_rate_view_previous_charge_rate);

		mApplyShoulderingRateCheckBox.setChecked(mChargingRate.isApplyShoulderingRate());
		mApplyShoulderingRateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mChargingRate.setApplyShoulderingRate(isChecked);
			}
		});

		mViewPreviousChargeRateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updatePreviousChargeRate();
			}
		});

		mHistogramView.setHorizontalTexts(LTADataStore.getHorizontalTexts());
		mHistogramView.setVerticalTexts(LTADataStore.getVerticalTexts());

		updateUI();

		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		this.savePriceValues();
	}

	public void savePriceValues() {
		ChargingRecord[] chargingPriceValues = null;
		if (mIsWeekday) {
			chargingPriceValues = this.mChargingRate.getWeekdayChargingRecords();
		} else {
			chargingPriceValues = this.mChargingRate.getSaturdayChargingRecords();
		}

		this.applyShoulderingForRecords(chargingPriceValues);
	}

	private void applyShoulderingForRecords(ChargingRecord[] chargingPriceValues) {
		int[] heightValues = mHistogramView.getHeightValues();

		for (int i = 0; i < chargingPriceValues.length; i++) {
			ChargingRecord record = chargingPriceValues[i];
			record.setValue(heightValues[i]);
			if (i != 0) {
				ChargingRecord prevRecord = chargingPriceValues[i - 1];
				if (record.getValue() * LTADataStore.PRINCE_INTERVAL - prevRecord.getValue()
						* LTADataStore.PRINCE_INTERVAL >= 1) {
					if (i != chargingPriceValues.length - 1) {
						ChargingRecord nextRecord = chargingPriceValues[i + 1];
						if (nextRecord.getValue() * LTADataStore.PRINCE_INTERVAL - record.getValue()
								* LTADataStore.PRINCE_INTERVAL <= -1) {
							record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS_NEXT);
							record.setPrevRecordValue(prevRecord.getValue());
							record.setNextRecordValue(nextRecord.getValue());
						} else {
							record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS);
							record.setPrevRecordValue(prevRecord.getValue());
						}
					} else {
						record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS);
						record.setPrevRecordValue(prevRecord.getValue());
					}
				} else {
					if (i != chargingPriceValues.length - 1) {
						ChargingRecord nextRecord = chargingPriceValues[i + 1];
						if (nextRecord.getValue() * LTADataStore.PRINCE_INTERVAL - record.getValue()
								* LTADataStore.PRINCE_INTERVAL <= -1) {
							record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_NEXT);
							record.setNextRecordValue(nextRecord.getValue());
						} else {
							record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_NONE);
						}
					} else {
						record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_NONE);
					}
				}
			} else {
				if (i != chargingPriceValues.length - 1) {
					ChargingRecord nextRecord = chargingPriceValues[i + 1];
					if (nextRecord.getValue() * LTADataStore.PRINCE_INTERVAL - record.getValue()
							* LTADataStore.PRINCE_INTERVAL <= -1) {
						record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_NEXT);
						record.setNextRecordValue(nextRecord.getValue());
					} else {
						record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_NONE);
					}
				} else {
					record.setShoulderingIndicator(ChargingRecord.SHOULDERING_INDICATOR_NONE);
				}
			}
		}
	}

	protected void updateUI() {
		mApplyShoulderingRateCheckBox.setTextSize(10);
		mViewPreviousChargeRateCheckBox.setTextSize(10);

		// mSaveTextView.setBackgroundColor(0xffff8917);
		// mSaveTextView.setTextColor(Color.WHITE);

		mHistogramView.setHeightValues(this.getHeightValues());
	}

	private void updatePreviousChargeRate() {
		if (mViewPreviousChargeRateCheckBox.isChecked()) {
			RateTemplate previousTemplate = LTADataStore.get().getPreviousRateTemplate(mRateTemplateId);

			if (previousTemplate != null) {
				ChargingRecord[] previousTemplateRecords;
				if (mIsWeekday) {
					previousTemplateRecords = previousTemplate.getChargingRate().getWeekdayChargingRecords();
				} else {
					previousTemplateRecords = previousTemplate.getChargingRate().getSaturdayChargingRecords();
				}

				float[] previousTemplateHeightValues = new float[LTADataStore.HORIZONTAL_COUNT - 1];
				String[] previousTampletHeightValueTexts = new String[LTADataStore.HORIZONTAL_COUNT - 1];

				for (int i = 0; i < previousTemplateRecords.length; i++) {

					if (previousTemplateRecords != null) {
						ChargingRecord prevTemplateRecord = previousTemplateRecords[i];
						previousTemplateHeightValues[i] = prevTemplateRecord.getValue()
								/ (float) LTADataStore.VERTICAL_COUNT;
						previousTampletHeightValueTexts[i] = LTADataStore.get().getPriceString(
								prevTemplateRecord.getValue());
					}
				}

				mHistogramView.setPreviousRateHeightValues(previousTemplateHeightValues);
				mHistogramView.setPreviousRateHeightValueTexts(previousTampletHeightValueTexts);
				mHistogramView.invalidate();
			}
		} else {
			mHistogramView.setPreviousRateHeightValues(null);
			mHistogramView.setPreviousRateHeightValueTexts(null);
			mHistogramView.invalidate();
		}
	}

	private int[] getHeightValues() {
		ChargingRecord[] chargingPriceValues = null;
		if (mIsWeekday) {
			chargingPriceValues = this.mChargingRate.getWeekdayChargingRecords();
		} else {
			chargingPriceValues = this.mChargingRate.getSaturdayChargingRecords();
		}

		int count = chargingPriceValues.length;
		int[] values = new int[count];
		for (int i = 0; i < count; i++) {
			ChargingRecord record = chargingPriceValues[i];
			values[i] = record.getValue();
		}
		return values;
	}

	public void setChargingRate(ChargingRate chargingRate) {
		mChargingRate = chargingRate;
	}

	public void setIsWeekday(boolean isWeekday) {
		this.savePriceValues();
		this.mIsWeekday = isWeekday;
		this.updateUI();
		this.updatePreviousChargeRate();
	}

	public void setRateTemplateId(String rateTemplateId) {
		mRateTemplateId = rateTemplateId;
	}
}
