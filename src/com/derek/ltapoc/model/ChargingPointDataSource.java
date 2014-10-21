package com.derek.ltapoc.model;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ChargingPointDataSource {
	private SQLiteDatabase database;
	private LTASQLiteHelper dbHelper;

	public ChargingPointDataSource(Context context) {
		dbHelper = new LTASQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean createChargingPoint(ChargingPoint chargingPoint) {
		ContentValues values = new ContentValues();

		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ID, chargingPoint.getChargingPointId());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_LATITUDE, chargingPoint.getLatitude());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_LONGITUDE, chargingPoint.getLongitude());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ROAD_NAME, chargingPoint.getRoadName());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_RATE_TEMPLATE_ID, chargingPoint.getRateTemplateId());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_EFFECTIVE_DATE, chargingPoint.getEffectiveDate()
				.getTime());

		long insertId = database.insert(LTASQLiteHelper.TABLE_CHARGING_POINT, null, values);

		return insertId > 0;
	}

	public ArrayList<ChargingPoint> getAllChargingPoints() {
		ArrayList<ChargingPoint> chargingPoints = new ArrayList<ChargingPoint>();

		String[] allColumns = { LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ID,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_LATITUDE,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_LONGITUDE,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ROAD_NAME,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_RATE_TEMPLATE_ID,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_EFFECTIVE_DATE };

		Cursor cursor = database.query(LTASQLiteHelper.TABLE_CHARGING_POINT, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			ChargingPoint point = new ChargingPoint();

			point.setChargingPointId(cursor.getString(0));
			point.setLatitude(cursor.getDouble(1));
			point.setLongitude(cursor.getDouble(2));
			point.setRoadName(cursor.getString(3));
			point.setRateTemplateId(cursor.getString(4));
			point.setEffectiveDate(new Date(cursor.getLong(5)));

			chargingPoints.add(point);

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();

		return chargingPoints;
	}

	public boolean updateChargingPoint(ChargingPoint point) {
		ContentValues values = new ContentValues();

		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ID, point.getChargingPointId());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_LATITUDE, point.getLatitude());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_LONGITUDE, point.getLongitude());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ROAD_NAME, point.getRoadName());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_RATE_TEMPLATE_ID, point.getRateTemplateId());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_EFFECTIVE_DATE, point.getEffectiveDate().getTime());
		values.put(LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ID, point.getChargingPointId());

		long rowId = database.update(LTASQLiteHelper.TABLE_CHARGING_POINT, values,
				LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ID + " = " + "'" + point.getChargingPointId() + "'", null);

		return rowId > 0;
	}

	public boolean deleteChargingPoint(ChargingPoint chargingPoint) {
		long rowId = database
				.delete(LTASQLiteHelper.TABLE_CHARGING_POINT, LTASQLiteHelper.COLUMN_TABLE_CHARGING_POINT_ID + " = "
						+ "'" + chargingPoint.getChargingPointId() + "'", null);
		return rowId > 0;
	}
}
