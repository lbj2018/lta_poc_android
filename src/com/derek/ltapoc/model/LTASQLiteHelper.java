package com.derek.ltapoc.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LTASQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_RATE_TEMPLATE = "rate_template";
	public static final String COLUMN_TABLE_RATE_TEMPLATE_ID = "_id";
	public static final String COLUMN_TABLE_RATE_TEMPLATE_SCHEME_NAME = "_scheme_name";
	public static final String COLUMN_TABLE_RATE_TEMPLATE_SCHEME_DESCRIPTION = "_scheme_description";
	public static final String COLUMN_TABLE_RATE_TEMPLATE_CREATED_BY = "_created_by";
	public static final String COLUMN_TABLE_RATE_TEMPLATE_CREATE_DATE = "_create_date";
	public static final String COLUMN_TABLE_RATE_TEMPLATE_APPLY_SHOULDERING_RATE = "_apply_shouldering_rate";

	public static final String TABLE_CHARGING_RECORD = "charging_record";
	// 0 for saturday, 1 for weekday
	public static final String COLUMN_TABLE_CHARGING_RECORD_TYPE = "_type";
	public static final String COLUMN_TABLE_CHARGING_RECORD_INDEX = "_index";
	public static final String COLUMN_TABLE_CHARGING_RECORD_VALUE = "_value";
	public static final String COLUMN_TABLE_CHARGING_RECORD_SHOULDERING_INDICATOR = "_shouldering_indicator";
	public static final String COLUMN_TABLE_CHARGING_RECORD_PREV_VALUE = "_prev_value";
	public static final String COLUMN_TABLE_CHARGING_RECORD_NEXT_VALUE = "_next_value";
	public static final String COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID = "_rate_template_id";

	private static final String DATABASE_NAME = "commments.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE_RATE_TEMPLATE = "create table " + TABLE_RATE_TEMPLATE + "("
			+ COLUMN_TABLE_RATE_TEMPLATE_ID + " text, " + COLUMN_TABLE_RATE_TEMPLATE_SCHEME_NAME + " text not null, "
			+ COLUMN_TABLE_RATE_TEMPLATE_SCHEME_DESCRIPTION + " text, " + COLUMN_TABLE_RATE_TEMPLATE_CREATED_BY
			+ " text, " + COLUMN_TABLE_RATE_TEMPLATE_APPLY_SHOULDERING_RATE + " integer, "
			+ COLUMN_TABLE_RATE_TEMPLATE_CREATE_DATE + " integer);";

	private static final String DATABASE_CREATE_CHARGING_RECORD = "create table " + TABLE_CHARGING_RECORD + "("
			+ COLUMN_TABLE_CHARGING_RECORD_TYPE + " integer, " + COLUMN_TABLE_CHARGING_RECORD_INDEX + " integer, "
			+ COLUMN_TABLE_CHARGING_RECORD_VALUE + " integer, " + COLUMN_TABLE_CHARGING_RECORD_SHOULDERING_INDICATOR
			+ " integer, " + COLUMN_TABLE_CHARGING_RECORD_PREV_VALUE + " integer, "
			+ COLUMN_TABLE_CHARGING_RECORD_NEXT_VALUE + " integer, " + COLUMN_TABLE_CHARGING_RECORD_RATE_TEMPLATE_ID
			+ " text);";

	public LTASQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_RATE_TEMPLATE);
		database.execSQL(DATABASE_CREATE_CHARGING_RECORD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LTASQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_RATE_TEMPLATE);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_CHARGING_RECORD);
		onCreate(db);
	}

}
