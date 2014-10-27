package com.derek.ltapoc;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.derek.ltapoc.DatePickerDialogFragment.DatePickerDialogFragmentCallbacks;
import com.derek.ltapoc.model.ChargingPoint;
import com.derek.ltapoc.model.DateFormatUtil;
import com.derek.ltapoc.model.LTADataStore;
import com.derek.ltapoc.model.RateTemplate;

public class EditChargingPointDialogFragment extends DialogFragment implements DatePickerDialogFragmentCallbacks {
	private EditText mRoadNameEditText;
	private TextView mLatitudeTextView;
	private TextView mLongitudeTextView;
	private TextView mEffectiveDateTextView;
	private EditText mMessageEditText;
	private EditText mRemarksEditText;
	private Spinner mRateTemplateSpinner;
	private ChargingPoint mChargingPoint;
	private ArrayList<RateTemplate> mRateTemplates;
	private EditChargingPointDialogFragmentCallbacks mCallbacks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mChargingPoint.getEffectiveDate() == null)
			mChargingPoint.setEffectiveDate(new Date());
		mRateTemplates = LTADataStore.get().getRateTemplates();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View rootView = inflater.inflate(R.layout.dialog_edit_charging_point, null);
		mRoadNameEditText = (EditText) rootView.findViewById(R.id.edit_text_road_name);
		mMessageEditText = (EditText) rootView.findViewById(R.id.edit_text_notification_message);
		mRemarksEditText = (EditText) rootView.findViewById(R.id.edit_text_charging_point_remarks);
		mLatitudeTextView = (TextView) rootView.findViewById(R.id.text_view_latitude);
		mLongitudeTextView = (TextView) rootView.findViewById(R.id.text_view_longitude);
		mEffectiveDateTextView = (TextView) rootView.findViewById(R.id.text_view_effective_date);
		mRateTemplateSpinner = (Spinner) rootView.findViewById(R.id.spinner_rate_template);

		mRoadNameEditText.setText(mChargingPoint.getRoadName());
		mMessageEditText.setText(mChargingPoint.getNotificationMessage());
		mRemarksEditText.setText(mChargingPoint.getRemarks());

		Date date = (mChargingPoint.getEffectiveDate() == null) ? new Date() : mChargingPoint.getEffectiveDate();
		String dateString = DateFormatUtil.getDateStringFromDate(date, "dd/MM/yyyy");
		mEffectiveDateTextView.setText(dateString);

		Button pickEffectiveDateButton = (Button) rootView.findViewById(R.id.button_effective_date_picker);
		pickEffectiveDateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment();
				dialogFragment.setCallbacks(EditChargingPointDialogFragment.this);
				dialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
			}
		});

		mLatitudeTextView.setText(mChargingPoint.getLatitude() + "");
		mLongitudeTextView.setText(mChargingPoint.getLongitude() + "");

		RateTemplateAdapter adapter = new RateTemplateAdapter(mRateTemplates);
		mRateTemplateSpinner.setAdapter(adapter);

		if (mChargingPoint.getRateTemplateId() != null) {
			RateTemplate selectedRateTemplate = LTADataStore.get().getRateTemplate(mChargingPoint.getRateTemplateId());
			int position = mRateTemplates.indexOf(selectedRateTemplate);
			mRateTemplateSpinner.setSelection(position);
		}

		builder.setView(rootView);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				String roadName = mRoadNameEditText.getText().toString();
				String message = mMessageEditText.getText().toString();
				String remarks = mRemarksEditText.getText().toString();

				if (roadName != null && roadName.length() != 0) {
					if (mRateTemplates != null && mRateTemplates.size() != 0) {
						int position = mRateTemplateSpinner.getSelectedItemPosition();
						RateTemplate rateTemplate = mRateTemplates.get(position);

						if (rateTemplate != null)
							mChargingPoint.setRateTemplateId(rateTemplate.getRateTemplateId());

						mChargingPoint.setRoadName(roadName);
						mChargingPoint.setNotificationMessage(message);
						mChargingPoint.setRemarks(remarks);

						mCallbacks.onDidSaveChargingPoint(mChargingPoint);
					} else {
						Toast.makeText(getActivity(), "Please create a rate template firstly.", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(getActivity(), "Please input road name.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				EditChargingPointDialogFragment.this.getDialog().cancel();
				mCallbacks.onDidCancelChargingPoint(mChargingPoint);
			}
		});

		return builder.create();
	}

	private class RateTemplateAdapter extends ArrayAdapter<RateTemplate> {
		public RateTemplateAdapter(ArrayList<RateTemplate> items) {
			super(getActivity(), android.R.layout.simple_spinner_item, items);
		}
	}

	public interface EditChargingPointDialogFragmentCallbacks {
		void onDidSaveChargingPoint(ChargingPoint point);

		void onDidCancelChargingPoint(ChargingPoint point);
	}

	public void setCallbacks(EditChargingPointDialogFragmentCallbacks callbacks) {
		mCallbacks = callbacks;
	}

	public void setChargingPoint(ChargingPoint chargingPoint) {
		mChargingPoint = chargingPoint;
	}

	@Override
	public void onPickDate(Date date) {
		mChargingPoint.setEffectiveDate(date);

		String dateString = DateFormatUtil.getDateStringFromDate(date, "dd/MM/yyyy");
		mEffectiveDateTextView.setText(dateString);
	}
}
