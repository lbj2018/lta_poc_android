package com.derek.ltapoc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.derek.ltapoc.model.ChargingPoint;
import com.derek.ltapoc.model.LTADataStore;

public class EditChargingPointDialogFragment extends DialogFragment {
	private TextView mLatitudeTextView;
	private TextView mLongitudeTextView;
	private ChargingPoint mChargingPoint;
	public static final String CHARGING_POINT_ID = "com.derek.ltapoc.CHARGING_POINT_ID";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String chargingPointId = getArguments().getString(CHARGING_POINT_ID);
		mChargingPoint = LTADataStore.get().getChargingPoint(chargingPointId);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View rootView = inflater.inflate(R.layout.dialog_edit_charging_point, null);
		mLatitudeTextView = (TextView) rootView.findViewById(R.id.text_view_latitude);
		mLongitudeTextView = (TextView) rootView.findViewById(R.id.text_view_longitude);

		mLatitudeTextView.setText(mChargingPoint.getLatitude() + "");
		mLongitudeTextView.setText(mChargingPoint.getLongitude() + "");

		builder.setView(rootView);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				EditChargingPointDialogFragment.this.getDialog().cancel();
			}
		});

		return builder.create();
	}
}
