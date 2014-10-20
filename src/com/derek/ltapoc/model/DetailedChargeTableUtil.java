package com.derek.ltapoc.model;

import java.util.ArrayList;

public class DetailedChargeTableUtil {

	public static ArrayList<ChargingRecord> getWeekdayPriceRecordsForNotEmpty(ChargingRate chargingRate) {
		ArrayList<ChargingRecord> priceValues = new ArrayList<ChargingRecord>();

		for (int i = 0; i < chargingRate.getWeekdayChargingRecords().length; i++) {
			ChargingRecord record = chargingRate.getWeekdayChargingRecords()[i];
			if (record.getValue() > 0) {
				priceValues.add(record);
			}
		}

		return priceValues;
	}

	public static ArrayList<ChargingRecord> getSaturdayPriceRecordsForNotEmpty(ChargingRate chargingRate) {
		ArrayList<ChargingRecord> priceValues = new ArrayList<ChargingRecord>();

		for (int i = 0; i < chargingRate.getSaturdayChargingRecords().length; i++) {
			ChargingRecord record = chargingRate.getSaturdayChargingRecords()[i];
			if (record.getValue() > 0) {
				priceValues.add(record);
			}
		}

		return priceValues;
	}

	private static int getWeekdayCountOfRecords(ArrayList<ChargingRecord> weekdayPriceRecords,
			boolean applyShoulderingRate) {
		int count = 0;
		if (applyShoulderingRate) {
			for (ChargingRecord record : weekdayPriceRecords) {
				if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NONE) {
					count += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS
						|| record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NEXT) {
					count += 2;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS_NEXT) {
					count += 3;
				}
			}
		} else {
			count = weekdayPriceRecords.size();
		}
		return count;
	}

	private static int getSaturdayCountOfRecords(ArrayList<ChargingRecord> saturdayPriceRecords,
			boolean applyShoulderingRate) {
		int count = 0;
		if (applyShoulderingRate) {
			for (ChargingRecord record : saturdayPriceRecords) {
				if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NONE) {
					count += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS
						|| record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NEXT) {
					count += 2;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS_NEXT) {
					count += 3;
				}
			}
		} else {
			count = saturdayPriceRecords.size();
		}
		return count;
	}

	public static int getCountOfRecords(ChargingRate chargingRate, boolean applyShoulderingRate) {
		ArrayList<ChargingRecord> weekdayPriceRecords = getWeekdayPriceRecordsForNotEmpty(chargingRate);
		ArrayList<ChargingRecord> saturdayPriceRecords = getSaturdayPriceRecordsForNotEmpty(chargingRate);

		int weekdayCount = getWeekdayCountOfRecords(weekdayPriceRecords, applyShoulderingRate);
		int saturdayCount = getSaturdayCountOfRecords(saturdayPriceRecords, applyShoulderingRate);
		return weekdayCount + saturdayCount;
	}

	public static String[] getVerticalTitles(ChargingRate chargingRate, boolean applyShoulderingRate) {
		ArrayList<ChargingRecord> weekdayPriceRecords = getWeekdayPriceRecordsForNotEmpty(chargingRate);
		ArrayList<ChargingRecord> saturdayPriceRecords = getSaturdayPriceRecordsForNotEmpty(chargingRate);

		int weekdayCount = getWeekdayCountOfRecords(weekdayPriceRecords, applyShoulderingRate);
		int saturdayCount = getSaturdayCountOfRecords(saturdayPriceRecords, applyShoulderingRate);

		String[] verticalTitles = new String[weekdayCount + saturdayCount];
		for (int i = 0; i < weekdayCount; i++) {
			verticalTitles[i] = "Weekday";
		}
		for (int i = 0; i < saturdayCount; i++) {
			verticalTitles[i + weekdayCount] = "Saturday";
		}

		return verticalTitles;
	}

	private static String[][] getWeekdayPriceValueTexts(ArrayList<ChargingRecord> weekdayPriceRecords,
			int detailedChargeTableHorizontalCount, boolean applyShoulderingRate) {
		int weekdayCount = getWeekdayCountOfRecords(weekdayPriceRecords, applyShoulderingRate);
		String[][] texts = new String[weekdayCount][detailedChargeTableHorizontalCount - 1];

		if (applyShoulderingRate) {
			int indicator = 0;
			for (int i = 0; i < weekdayPriceRecords.size(); i++) {
				ChargingRecord record = weekdayPriceRecords.get(i);
				if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NONE) {
					int index = record.getIndex();
					float value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS) {
					int index = record.getIndex();
					float value = record.getPrevRecordValue() + (record.getValue() - record.getPrevRecordValue())
							/ 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NEXT) {
					int index = record.getIndex();
					float value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue() - (record.getValue() - record.getNextRecordValue()) / 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS_NEXT) {
					int index = record.getIndex();
					float value = record.getPrevRecordValue() + (record.getValue() - record.getPrevRecordValue())
							/ 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue() - (record.getValue() - record.getNextRecordValue()) / 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				}
			}
		} else {
			int indicator = 0;
			for (int i = 0; i < weekdayPriceRecords.size(); i++) {
				ChargingRecord record = weekdayPriceRecords.get(i);
				int index = record.getIndex();
				int value = record.getValue();
				texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
				texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
				texts[indicator][2] = LTADataStore.get().getPriceString(value);
				texts[indicator][3] = LTADataStore.get().getPriceString(value);
				texts[indicator][4] = LTADataStore.get().getPriceString(value);
				texts[indicator][5] = LTADataStore.get().getPriceString(value);
				texts[indicator][6] = LTADataStore.get().getPriceString(value);

				indicator += 1;
			}
		}
		return texts;
	}

	private static String[][] getSaturdayPriceValueTexts(ArrayList<ChargingRecord> saturdayPriceRecords,
			int detailedChargeTableHorizontalCount, boolean applyShoulderingRate) {
		int saturdayCount = getSaturdayCountOfRecords(saturdayPriceRecords, applyShoulderingRate);
		String[][] texts = new String[saturdayCount][detailedChargeTableHorizontalCount - 1];

		if (applyShoulderingRate) {
			int indicator = 0;
			for (int i = 0; i < saturdayPriceRecords.size(); i++) {
				ChargingRecord record = saturdayPriceRecords.get(i);
				if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NONE) {
					int index = record.getIndex();
					float value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS) {
					int index = record.getIndex();
					float value = record.getPrevRecordValue() + (record.getValue() - record.getPrevRecordValue())
							/ 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_NEXT) {
					int index = record.getIndex();
					float value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue() - (record.getValue() - record.getNextRecordValue()) / 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				} else if (record.getShoulderingIndicator() == ChargingRecord.SHOULDERING_INDICATOR_PREVIOUS_NEXT) {
					int index = record.getIndex();
					float value = record.getPrevRecordValue() + (record.getValue() - record.getPrevRecordValue())
							/ 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue();
					texts[indicator][0] = LTADataStore.get().getTimeString(
							index * LTADataStore.TIME_INTERVAL + LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;

					index = record.getIndex();
					value = record.getValue() - (record.getValue() - record.getNextRecordValue()) / 2.0f;
					texts[indicator][0] = LTADataStore.get().getTimeString(
							(index + 1) * LTADataStore.TIME_INTERVAL - LTADataStore.SHOULDERING_RATE_TIME_INTERVAL);
					texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
					texts[indicator][2] = LTADataStore.get().getPriceString(value);
					texts[indicator][3] = LTADataStore.get().getPriceString(value);
					texts[indicator][4] = LTADataStore.get().getPriceString(value);
					texts[indicator][5] = LTADataStore.get().getPriceString(value);
					texts[indicator][6] = LTADataStore.get().getPriceString(value);

					indicator += 1;
				}
			}
		} else {
			int indicator = 0;
			for (int i = 0; i < saturdayPriceRecords.size(); i++) {
				ChargingRecord record = saturdayPriceRecords.get(i);
				int index = record.getIndex();
				int value = record.getValue();
				texts[indicator][0] = LTADataStore.get().getTimeString(index * LTADataStore.TIME_INTERVAL);
				texts[indicator][1] = LTADataStore.get().getTimeString((index + 1) * LTADataStore.TIME_INTERVAL);
				texts[indicator][2] = LTADataStore.get().getPriceString(value);
				texts[indicator][3] = LTADataStore.get().getPriceString(value);
				texts[indicator][4] = LTADataStore.get().getPriceString(value);
				texts[indicator][5] = LTADataStore.get().getPriceString(value);
				texts[indicator][6] = LTADataStore.get().getPriceString(value);

				indicator += 1;
			}
		}
		return texts;
	}

	public static String[][] getPriceValueTexts(ChargingRate chargingRate, int detailedChargeTableHorizontalCount,
			boolean applyShoulderingRate) {

		ArrayList<ChargingRecord> weekdayPriceRecords = getWeekdayPriceRecordsForNotEmpty(chargingRate);
		ArrayList<ChargingRecord> saturdayPriceRecords = getSaturdayPriceRecordsForNotEmpty(chargingRate);

		String[][] weekdayTexts = getWeekdayPriceValueTexts(weekdayPriceRecords, detailedChargeTableHorizontalCount,
				applyShoulderingRate);
		String[][] saturdayTexts = getSaturdayPriceValueTexts(saturdayPriceRecords, detailedChargeTableHorizontalCount,
				applyShoulderingRate);

		int weekdayCount = getWeekdayCountOfRecords(weekdayPriceRecords, applyShoulderingRate);
		int saturdayCount = getSaturdayCountOfRecords(saturdayPriceRecords, applyShoulderingRate);
		String[][] texts = new String[weekdayCount + saturdayCount][detailedChargeTableHorizontalCount - 1];

		for (int i = 0; i < weekdayCount; i++) {
			texts[i] = weekdayTexts[i];
		}
		for (int i = 0; i < saturdayCount; i++) {
			texts[weekdayCount + i] = saturdayTexts[i];
		}

		// int count = weekdayCount + saturdayCount;
		//
		// String[][] texts = new
		// String[count][detailedChargeTableHorizontalCount - 1];
		// for (int i = 0; i < weekdayPriceRecords.size(); i++) {
		// ChargingRecord record = weekdayPriceRecords.get(i);
		// int index = record.getIndex();
		// int value = record.getValue();
		// texts[i][0] = LTADataStore.get().getTimeString(index *
		// LTADataStore.TIME_INTERVAL);
		// texts[i][1] = LTADataStore.get().getTimeString((index + 1) *
		// LTADataStore.TIME_INTERVAL);
		// texts[i][2] = LTADataStore.get().getPriceString(value);
		// texts[i][3] = LTADataStore.get().getPriceString(value);
		// texts[i][4] = LTADataStore.get().getPriceString(value);
		// texts[i][5] = LTADataStore.get().getPriceString(value);
		// texts[i][6] = LTADataStore.get().getPriceString(value);
		// }
		// for (int i = 0; i < saturdayPriceRecords.size(); i++) {
		// ChargingRecord record = saturdayPriceRecords.get(i);
		// int index = record.getIndex();
		// int value = record.getValue();
		// texts[i + weekdayPriceRecords.size()][0] =
		// LTADataStore.get().getTimeString(
		// index * LTADataStore.TIME_INTERVAL);
		// texts[i + weekdayPriceRecords.size()][1] =
		// LTADataStore.get().getTimeString(
		// (index + 1) * LTADataStore.TIME_INTERVAL);
		// texts[i + weekdayPriceRecords.size()][2] =
		// LTADataStore.get().getPriceString(value);
		// texts[i + weekdayPriceRecords.size()][3] =
		// LTADataStore.get().getPriceString(value);
		// texts[i + weekdayPriceRecords.size()][4] =
		// LTADataStore.get().getPriceString(value);
		// texts[i + weekdayPriceRecords.size()][5] =
		// LTADataStore.get().getPriceString(value);
		// texts[i + weekdayPriceRecords.size()][6] =
		// LTADataStore.get().getPriceString(value);
		// }

		return texts;
	}
}
