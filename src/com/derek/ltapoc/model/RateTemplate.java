package com.derek.ltapoc.model;

import java.util.UUID;

public class RateTemplate {
	private String mRateTemplateId;
	private ChargingScheme mChargingScheme;
	private ChargingRate mChargingRate;

	public RateTemplate() {
	}

	public RateTemplate(int count) {
		mRateTemplateId = UUID.randomUUID().toString();
		mChargingScheme = new ChargingScheme();
		mChargingRate = new ChargingRate(count);
	}

	public String getRateTemplateId() {
		return mRateTemplateId;
	}

	public void setRateTemplateId(String rateTemplateId) {
		mRateTemplateId = rateTemplateId;
	}

	public ChargingScheme getChargingScheme() {
		return mChargingScheme;
	}

	public void setChargingScheme(ChargingScheme chargingScheme) {
		mChargingScheme = chargingScheme;
	}

	public ChargingRate getChargingRate() {
		return mChargingRate;
	}

	public void setChargingRate(ChargingRate chargingRate) {
		mChargingRate = chargingRate;
	}

	@Override
	public String toString() {
		return mChargingScheme.getSchemeName();
	}
}
