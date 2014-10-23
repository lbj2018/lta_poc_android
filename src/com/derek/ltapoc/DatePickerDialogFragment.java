package com.derek.ltapoc;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	private DatePickerDialogFragmentCallbacks mCallbacks;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int monthOfYear = c.get(Calendar.MONTH);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		@SuppressWarnings("deprecation")
		Date date = new Date(year, monthOfYear, dayOfMonth);
		mCallbacks.onPickDate(date);
	}

	public interface DatePickerDialogFragmentCallbacks {
		void onPickDate(Date date);
	}

	public void setCallbacks(DatePickerDialogFragmentCallbacks callbacks) {
		mCallbacks = callbacks;
	}
}
