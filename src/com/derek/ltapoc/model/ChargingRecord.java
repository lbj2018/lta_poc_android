package com.derek.ltapoc.model;

public class ChargingRecord {
	public static final int SHOULDERING_INDICATOR_NONE = 0;
	public static final int SHOULDERING_INDICATOR_PREVIOUS = 1;
	public static final int SHOULDERING_INDICATOR_NEXT = 2;
	public static final int SHOULDERING_INDICATOR_PREVIOUS_NEXT = 3;
	private int mIndex;
	private int mValue;
	private int mShoulderingIndicator; // 1 for previous comparison 2 for next
										// comparison, 0 for no shouldering rate
	private int mPrevRecordValue;
	private int mNextRecordValue;

	public ChargingRecord() {
		this(0, 0, SHOULDERING_INDICATOR_NONE);
	}

	public ChargingRecord(int index, int value) {
		this(index, value, SHOULDERING_INDICATOR_NONE);
	}

	public ChargingRecord(int index, int value, int shoulderingIndicator) {
		mIndex = index;
		mValue = value;
		mShoulderingIndicator = shoulderingIndicator;
	}

	public void configure(ChargingRecord record) {
		this.mIndex = record.getIndex();
		this.mValue = record.getValue();
		this.mShoulderingIndicator = record.getShoulderingIndicator();
		this.mPrevRecordValue = record.getPrevRecordValue();
		this.mNextRecordValue = record.getNextRecordValue();
	}

	public int getIndex() {
		return mIndex;
	}

	public void setIndex(int index) {
		mIndex = index;
	}

	public int getValue() {
		return mValue;
	}

	public void setValue(int value) {
		mValue = value;
	}

	public int getShoulderingIndicator() {
		return mShoulderingIndicator;
	}

	public void setShoulderingIndicator(int shoulderingIndicator) {
		mShoulderingIndicator = shoulderingIndicator;
	}

	public int getPrevRecordValue() {
		return mPrevRecordValue;
	}

	public void setPrevRecordValue(int prevRecordValue) {
		mPrevRecordValue = prevRecordValue;
	}

	public int getNextRecordValue() {
		return mNextRecordValue;
	}

	public void setNextRecordValue(int nextRecordValue) {
		mNextRecordValue = nextRecordValue;
	}
}
