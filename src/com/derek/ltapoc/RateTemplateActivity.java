package com.derek.ltapoc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.derek.ltapoc.ReviewChangesFragment.ReviewChangesFragmentCallbacks;
import com.derek.ltapoc.SegmentedControlFragment.SegmentedControlFragmentCallbacks;
import com.derek.ltapoc.model.ChargingRate;
import com.derek.ltapoc.model.LTADataStore;
import com.derek.ltapoc.model.RateTemplate;
import com.derek.ltapoc.model.RateTemplateDataSource;

public class RateTemplateActivity extends Activity implements SegmentedControlFragmentCallbacks,
		ReviewChangesFragmentCallbacks {
	private ChargingSchemeFragment mChargingSchemeFragment;
	private SegmentedControlFragment mSegmentedControlFragment;
	private ChargingRateFragment mChargingRateFragment;
	private ReviewChangesFragment mReviewChangesFragment;
	private RateTemplate mRateTemplate;
	private ChargingRate mChargingRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String templateId = getIntent().getStringExtra("rateTemplateId");
		if (templateId != null) {
			mRateTemplate = LTADataStore.get().getRateTemplate(templateId);
		} else {
			mRateTemplate = new RateTemplate(LTADataStore.HORIZONTAL_COUNT - 1);
		}

		mChargingRate = new ChargingRate(mRateTemplate.getChargingRate());

		this.mChargingSchemeFragment = new ChargingSchemeFragment();
		this.mSegmentedControlFragment = new SegmentedControlFragment();
		this.mChargingRateFragment = new ChargingRateFragment();
		this.mReviewChangesFragment = new ReviewChangesFragment();

		this.mChargingSchemeFragment.setChargingScheme(mRateTemplate.getChargingScheme());
		this.mChargingRateFragment.setChargingRate(mChargingRate);
		this.mReviewChangesFragment.setChargingRate(mChargingRate);
		this.mReviewChangesFragment.setRateTemplateId(templateId);

		setContentView(R.layout.activity_rate_template);
		if (savedInstanceState == null) {
			FragmentManager fm = getFragmentManager();

			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.container_charging_scheme, this.mChargingSchemeFragment);
			ft.add(R.id.container_segmented_control, this.mSegmentedControlFragment);
			ft.add(R.id.container_charging_rate, this.mChargingRateFragment);
			ft.commit();
		}
	}

	@Override
	public void onWeekdayRateSelected() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		Fragment oldDetail = fm.findFragmentById(R.id.container_charging_rate);

		if (oldDetail != this.mChargingRateFragment) {
			if (oldDetail != null) {
				ft.remove(oldDetail);
			}

			ft.add(R.id.container_charging_rate, this.mChargingRateFragment);
			ft.commit();
		}

		this.mChargingRateFragment.setIsWeekday(true);
	}

	@Override
	public void onSaturdayRateSelected() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		Fragment oldDetail = fm.findFragmentById(R.id.container_charging_rate);

		if (oldDetail != this.mChargingRateFragment) {
			if (oldDetail != null) {
				ft.remove(oldDetail);
			}

			ft.add(R.id.container_charging_rate, this.mChargingRateFragment);
			ft.commit();
		}

		this.mChargingRateFragment.setIsWeekday(false);
	}

	@Override
	public void onReviewChangesRateSelected() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		Fragment oldDetail = fm.findFragmentById(R.id.container_charging_rate);

		if (oldDetail == this.mReviewChangesFragment) {
			return;
		}

		if (oldDetail != null) {
			ft.remove(oldDetail);
		}

		ft.add(R.id.container_charging_rate, this.mReviewChangesFragment);
		ft.commit();

		this.mChargingRateFragment.savePriceValues();
	}

	@Override
	public void submitRateTemplate() {

		if (mChargingSchemeFragment.submit()) {

			mRateTemplate.getChargingRate().configure(mChargingRate);

			if (!LTADataStore.get().getRateTemplates().contains(mRateTemplate)) {
				LTADataStore.get().getRateTemplates().add(0, mRateTemplate);

				RateTemplateDataSource dataSource = new RateTemplateDataSource(this);
				dataSource.open();
				dataSource.createRateTemplate(mRateTemplate);
				dataSource.close();
			} else {
				RateTemplateDataSource dataSource = new RateTemplateDataSource(this);
				dataSource.open();
				dataSource.updateRateTemplate(mRateTemplate);
				dataSource.close();
			}
			this.finish();
		}
	}
}
