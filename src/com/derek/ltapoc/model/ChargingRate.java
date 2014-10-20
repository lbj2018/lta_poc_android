package com.derek.ltapoc.model;

public class ChargingRate {
	private int mCount;
	private boolean mApplyShoulderingRate;
	private ChargingRecord[] mWeekdayChargingRecords;
	private ChargingRecord[] mSaturdayChargingRecords;

	public ChargingRate() {

	}

	public ChargingRate(int count) {
		this.mCount = count;

		mApplyShoulderingRate = true;
		mWeekdayChargingRecords = new ChargingRecord[this.mCount];
		mSaturdayChargingRecords = new ChargingRecord[this.mCount];
		for (int i = 0; i < count; i++) {
			ChargingRecord weekdayRecord = new ChargingRecord(i, 0);
			ChargingRecord saturdayRecord = new ChargingRecord(i, 0);
			mWeekdayChargingRecords[i] = weekdayRecord;
			mSaturdayChargingRecords[i] = saturdayRecord;
		}
	}

	public ChargingRate(ChargingRate rate) {
		mCount = rate.getCount();
		mApplyShoulderingRate = rate.isApplyShoulderingRate();

		mWeekdayChargingRecords = new ChargingRecord[mCount];
		mSaturdayChargingRecords = new ChargingRecord[mCount];

		for (int i = 0; i < mCount; i++) {
			ChargingRecord oldWeekdayRecord = rate.getWeekdayChargingRecords()[i];
			ChargingRecord oldSaturdayRecord = rate.getSaturdayChargingRecords()[i];

			ChargingRecord weekdayRecord = new ChargingRecord(i, oldWeekdayRecord.getValue());
			weekdayRecord.setShoulderingIndicator(oldWeekdayRecord.getShoulderingIndicator());
			weekdayRecord.setPrevRecordValue(oldWeekdayRecord.getPrevRecordValue());
			weekdayRecord.setNextRecordValue(oldWeekdayRecord.getNextRecordValue());

			ChargingRecord saturdayRecord = new ChargingRecord(i, oldSaturdayRecord.getValue());
			saturdayRecord.setShoulderingIndicator(oldSaturdayRecord.getShoulderingIndicator());
			saturdayRecord.setPrevRecordValue(oldSaturdayRecord.getPrevRecordValue());
			saturdayRecord.setNextRecordValue(oldSaturdayRecord.getNextRecordValue());

			mWeekdayChargingRecords[i] = weekdayRecord;
			mSaturdayChargingRecords[i] = saturdayRecord;
		}
	}

	public void configure(ChargingRate rate) {
		mApplyShoulderingRate = rate.isApplyShoulderingRate();
		for (int i = 0; i < mCount; i++) {
			ChargingRecord oldWeekdayRecord = rate.getWeekdayChargingRecords()[i];
			ChargingRecord oldSaturdayRecord = rate.getSaturdayChargingRecords()[i];

			ChargingRecord weekdayRecord = getWeekdayChargingRecords()[i];
			ChargingRecord saturdayRecord = getSaturdayChargingRecords()[i];

			weekdayRecord.configure(oldWeekdayRecord);
			saturdayRecord.configure(oldSaturdayRecord);
		}
	}

	public ChargingRecord[] getWeekdayChargingRecords() {
		return mWeekdayChargingRecords;
	}

	public void setWeekdayChargingRecords(ChargingRecord[] weekdayChargingRecords) {
		mWeekdayChargingRecords = weekdayChargingRecords;
	}

	public ChargingRecord[] getSaturdayChargingRecords() {
		return mSaturdayChargingRecords;
	}

	public void setSaturdayChargingRecords(ChargingRecord[] saturdayChargingRecords) {
		mSaturdayChargingRecords = saturdayChargingRecords;
	}

	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		mCount = count;
	}

	public boolean isApplyShoulderingRate() {
		return mApplyShoulderingRate;
	}

	public void setApplyShoulderingRate(boolean applyShoulderingRate) {
		mApplyShoulderingRate = applyShoulderingRate;
	}
}
