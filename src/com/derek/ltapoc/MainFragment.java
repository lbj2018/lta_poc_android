package com.derek.ltapoc;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.derek.ltapoc.model.LTADataStore;
import com.derek.ltapoc.model.RateTemplate;
import com.derek.ltapoc.model.RateTemplateDataSource;

public class MainFragment extends Fragment {
	private TextView rateTemplateTextView;
	private TextView chargingPointTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RateTemplateDataSource dataSource = new RateTemplateDataSource(getActivity());
		dataSource.open();
		ArrayList<RateTemplate> templates = dataSource.getAllRateTemplates();
		dataSource.close();

		if (templates != null) {
			LTADataStore.get().setRateTemplates(templates);
		}
	}

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
