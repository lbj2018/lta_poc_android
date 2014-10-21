package com.derek.ltapoc.model;

import java.util.Date;

public class ChargingPoint {
	private String mChargingPointId;
	private double mLatitude;
	private double mLongitude;
	private String mRoadName;
	private String rateTemplateId;
	private Date mEffectiveDate;

	public ChargingPoint() {
	}

	public String getChargingPointId() {
		return mChargingPointId;
	}

	public void setChargingPointId(String chargingPointId) {
		mChargingPointId = chargingPointId;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	public String getRoadName() {
		return mRoadName;
	}

	public void setRoadName(String roadName) {
		mRoadName = roadName;
	}

	public Date getEffectiveDate() {
		return mEffectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		mEffectiveDate = effectiveDate;
	}

	public String getRateTemplateId() {
		return rateTemplateId;
	}

	public void setRateTemplateId(String rateTemplateId) {
		this.rateTemplateId = rateTemplateId;
	}
}
