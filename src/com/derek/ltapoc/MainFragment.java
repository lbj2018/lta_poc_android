package com.derek.ltapoc;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
	private TextView rateTemplateTextView;
	private TextView chargingPointTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		rateTemplateTextView = (TextView) rootView.findViewById(R.id.text_view_main_rate_template);
		chargingPointTextView = (TextView) rootView.findViewById(R.id.text_view_main_charging_point);

		rateTemplateTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RateTemplateListActivity.class);
				startActivity(intent);
			}
		});

		chargingPointTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ChargingPointActivity.class);
				startActivity(intent);
			}
		});

		updateUI();

		return rootView;
	}

	private void updateUI() {
		rateTemplateTextView.setBackgroundColor(Color.LTGRAY);
		chargingPointTextView.setBackgroundColor(Color.LTGRAY);
	}
}
