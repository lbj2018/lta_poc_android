package com.derek.ltapoc.model;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RateTemplateDataSource {
	private SQLiteDatabase database;
	private LTASQLiteHelper dbHelper;

	public RateTemplateDataSource(Context context) {
		dbHelper = new LTASQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean createRateTemplate(RateTemplate template) {
		ContentValues values = new ContentValues();

		String templateId = template.getRateTemplateId();

		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_ID, templateId);
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_SCHEME_NAME, template.getChargingScheme().getSchemeName());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_SCHEME_DESCRIPTION, template.getChargingScheme()
				.getSchemeDescription());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_CREATED_BY, template.getChargingScheme().getCreatedBy());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_CREATE_DATE, template.getChargingScheme()
				.getCreatedDate().getTime());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_APPLY_SHOULDERING_RATE, template.getChargingRate()
				.isApplyShoulderingRate());

		long insertId = database.insert(LTASQLiteHelper.TABLE_RATE_TEMPLATE, null, values);

		if (insertId > 0) {
			ChargingRecord[] weekdayRecords = template.getChargingRate().getWeekdayChargingRecords();
			ChargingRecord[] saturRecords = template.getChargingRate().getSaturdayChargingRecords();
			for (int i = 0; i < weekdayRecords.length; i++) {
				ChargingRecord record = weekdayRecords[i];
				createChargingRecord(record, templateId, 1);
			}
			for (int i = 0; i < saturRecords.length; i++) {
				ChargingRecord record = saturRecords[i];
				createChargingRecord(record, templateId, 0);
			}
		}

		return insertId > 0;
	}

	private boolean createChargingRecord(ChargingRecord record, String templateId, int type) {
		ContentValues values = new ContentValues();
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_TYPE, type);
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_INDEX, record.getIndex());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_VALUE, record.getValue());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_SHOULDERING_INDICATOR, record.getShoulderingIndicator());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_PREV_VALUE, record.getPrevRecordValue());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_NEXT_VALUE, record.getNextRecordValue());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID, templateId);

		long insertId = database.insert(LTASQLiteHelper.TABLE_CHARGING_RECORD, null, values);
		return insertId > 0;
	}

	public ArrayList<RateTemplate> getAllRateTemplates() {
		ArrayList<RateTemplate> templates = new ArrayList<RateTemplate>();

		String[] allColumns = { LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_ID,
				LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_SCHEME_NAME,
				LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_SCHEME_DESCRIPTION,
				LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_CREATED_BY,
				LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_CREATE_DATE,
				LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_APPLY_SHOULDERING_RATE };

		Cursor cursor = database.query(LTASQLiteHelper.TABLE_RATE_TEMPLATE, allColumns, null, null, null, null,
				LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_CREATE_DATE);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			RateTemplate template = new RateTemplate();

			ChargingScheme scheme = new ChargingScheme();
			ChargingRate rate = new ChargingRate();

			template.setChargingScheme(scheme);
			template.setChargingRate(rate);

			String templateId = cursor.getString(0);
			template.setRateTemplateId(templateId);
			scheme.setSchemeName(cursor.getString(1));
			scheme.setSchemeDescription(cursor.getString(2));
			scheme.setCreatedBy(cursor.getString(3));
			scheme.setCreatedDate(new Date(cursor.getLong(4)));
			rate.setApplyShoulderingRate((cursor.getInt(5) != 0));

			//
			ArrayList<ChargingRecord> weekdayRecordList = getChargingRecords(templateId, 1);
			ArrayList<ChargingRecord> saturdayRecordList = getChargingRecords(templateId, 0);

			ChargingRecord[] weekdayRecords = new ChargingRecord[weekdayRecordList.size()];
			ChargingRecord[] saturdayRecords = new ChargingRecord[saturdayRecordList.size()];

			for (int i = 0; i < weekdayRecordList.size(); i++) {
				weekdayRecords[i] = weekdayRecordList.get(i);
			}
			for (int i = 0; i < saturdayRecordList.size(); i++) {
				saturdayRecords[i] = saturdayRecordList.get(i);
			}

			rate.setCount(weekdayRecordList.size());
			rate.setWeekdayChargingRecords(weekdayRecords);
			rate.setSaturdayChargingRecords(saturdayRecords);

			templates.add(0, template);

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();

		return templates;
	}

	private ArrayList<ChargingRecord> getChargingRecords(String templateId, int type) {
		ArrayList<ChargingRecord> records = new ArrayList<ChargingRecord>();

		String[] allColumns = { LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_TYPE,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_INDEX, LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_VALUE,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_SHOULDERING_INDICATOR,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_PREV_VALUE,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_NEXT_VALUE,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID };

		Cursor cursor = database.query(LTASQLiteHelper.TABLE_CHARGING_RECORD, allColumns,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID + " = " + "'" + templateId + "'"
						+ " and " + LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_TYPE + " = " + type, null, null, null,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_INDEX);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			ChargingRecord record = new ChargingRecord();
			record.setIndex(cursor.getInt(1));
			record.setValue(cursor.getInt(2));
			record.setShoulderingIndicator(cursor.getInt(3));
			record.setPrevRecordValue(cursor.getInt(4));
			record.setNextRecordValue(cursor.getInt(5));

			records.add(record);

			cursor.moveToNext();
		}
		cursor.close();

		return records;
	}

	public boolean updateRateTemplate(RateTemplate template) {
		ContentValues values = new ContentValues();

		String templateId = template.getRateTemplateId();

		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_ID, templateId);
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_SCHEME_NAME, template.getChargingScheme().getSchemeName());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_SCHEME_DESCRIPTION, template.getChargingScheme()
				.getSchemeDescription());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_CREATED_BY, template.getChargingScheme().getCreatedBy());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_CREATE_DATE, template.getChargingScheme()
				.getCreatedDate().getTime());
		values.put(LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_APPLY_SHOULDERING_RATE, template.getChargingRate()
				.isApplyShoulderingRate());

		long rowId = database.update(LTASQLiteHelper.TABLE_RATE_TEMPLATE, values,
				LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_ID + " = " + "'" + templateId + "'", null);

		if (rowId > 0) {
			ChargingRecord[] weekdayRecords = template.getChargingRate().getWeekdayChargingRecords();
			ChargingRecord[] saturRecords = template.getChargingRate().getSaturdayChargingRecords();
			for (int i = 0; i < weekdayRecords.length; i++) {
				ChargingRecord record = weekdayRecords[i];
				updateChargingRecord(record, 1, templateId);
			}
			for (int i = 0; i < saturRecords.length; i++) {
				ChargingRecord record = saturRecords[i];
				updateChargingRecord(record, 0, templateId);
			}
		}
		return rowId > 0;
	}

	private boolean updateChargingRecord(ChargingRecord record, int type, String templateId) {
		ContentValues values = new ContentValues();
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_TYPE, type);
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_INDEX, record.getIndex());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_VALUE, record.getValue());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_SHOULDERING_INDICATOR, record.getShoulderingIndicator());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_PREV_VALUE, record.getPrevRecordValue());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_NEXT_VALUE, record.getNextRecordValue());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID, templateId);

		long rowId = database.update(LTASQLiteHelper.TABLE_CHARGING_RECORD, values,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID + " = " + "'" + templateId + "'"
						+ " and " + LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_TYPE + " = " + type + " and "
						+ LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_INDEX + " = " + record.getIndex(), null);

		return rowId > 0;
	}

	public boolean deleteRateTemplate(RateTemplate template) {
		long rowId = database.delete(LTASQLiteHelper.TABLE_RATE_TEMPLATE, LTASQLiteHelper.COLUMN_TABLE_RATE_TEMPLATE_ID
				+ " = " + "'" + template.getRateTemplateId() + "'", null);

		if (rowId > 0) {
			deleteChargingRecordsForRateTemplate(template.getRateTemplateId());
		}

		return rowId > 0;
	}

	private boolean deleteChargingRecordsForRateTemplate(String templateId) {
		long rowId = database.delete(LTASQLiteHelper.TABLE_CHARGING_RECORD,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID + " = " + "'" + templateId + "'", null);
		return rowId > 0;
	}
}
