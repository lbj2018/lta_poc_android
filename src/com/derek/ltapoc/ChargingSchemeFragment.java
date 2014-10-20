package com.derek.ltapoc;

import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.derek.ltapoc.model.ChargingScheme;
import com.derek.ltapoc.model.DateFormatUtil;

public class ChargingSchemeFragment extends Fragment {
	private EditText schemaNameEditText;
	private EditText schemaDescriptionEditText;
	private EditText createdByEditText;
	private EditText createDateEditText;
	private ChargingScheme mChargingScheme;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_charging_scheme, container, false);

		schemaNameEditText = (EditText) rootView.findViewById(R.id.edit_text_schema_name);
		schemaDescriptionEditText = (EditText) rootView.findViewById(R.id.edit_text_schema_description);
		createdByEditText = (EditText) rootView.findViewById(R.id.edit_text_created_by);
		createDateEditText = (EditText) rootView.findViewById(R.id.edit_text_create_date);

		schemaNameEditText.setText("Scheme");
		createdByEditText.setText("zhou dengfeng derek");

		updateUI();

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		return rootView;
	}

	public boolean submit() {
		String schemeName = schemaNameEditText.getText().toString();
		String schemeDescription = schemaDescriptionEditText.getText().toString();
		String createdBy = createdByEditText.getText().toString();

		if (check(schemeName, schemeDescription, createdBy)) {
			mChargingScheme.setSchemeName(schemeName);
			mChargingScheme.setSchemeDescription(schemeDescription);
			mChargingScheme.setCreatedBy(createdBy);
			if (mChargingScheme.getCreatedDate() == null) {
				mChargingScheme.setCreatedDate(new Date());
			}
			return true;
		}
		return false;
	}

	private void updateUI() {
		schemaNameEditText.setText(mChargingScheme.getSchemeName());
		schemaDescriptionEditText.setText(mChargingScheme.getSchemeDescription());
		createdByEditText.setText(mChargingScheme.getCreatedBy());

		Date createDate = mChargingScheme.getCreatedDate();
		if (createDate == null) {
			createDate = new Date();
		}
		String dateString = DateFormatUtil.getDateStringFromDate(createDate, "dd/MM/yyyy HH:mm");
		createDateEditText.setText(dateString);
	}

	private boolean check(String schemeName, String schemeDescription, String createdBy) {
		if (schemeName == null || schemeName.length() == 0) {
			Toast.makeText(getActivity(), "Scheme name cannot be empty.", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (createdBy == null || createdBy.length() == 0) {
			Toast.makeText(getActivity(), "Created by cannot be empty.", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public void setChargingScheme(ChargingScheme chargingScheme) {
		mChargingScheme = chargingScheme;
	}
}
