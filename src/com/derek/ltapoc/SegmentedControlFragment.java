package com.derek.ltapoc;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SegmentedControlFragment extends Fragment implements View.OnClickListener {
	private SegmentedControlFragmentCallbacks callbacks;
	private TextView weekdayRateTextView;
	private TextView saturdayRateTextView;
	private TextView reviewChargesTextView;

	private int selectedSegmentedIndex; // 0 for weekday rate, 1 for saturday

	// rate, 2, review charges

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_segmented_control, container, false);

		weekdayRateTextView = (TextView) rootView.findViewById(R.id.text_view_weekday_rate);
		saturdayRateTextView = (TextView) rootView.findViewById(R.id.text_view_saturday_rate);
		reviewChargesTextView = (TextView) rootView.findViewById(R.id.text_view_review_changes);

		weekdayRateTextView.setOnClickListener(this);
		saturdayRateTextView.setOnClickListener(this);
		reviewChargesTextView.setOnClickListener(this);

		updateUI();

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callbacks = (SegmentedControlFragmentCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callbacks = null;
	}

	private void updateUI() {

		if (selectedSegmentedIndex == 0) {
			weekdayRateTextView.setTextColor(0xff85b8c7);
			saturdayRateTextView.setTextColor(Color.DKGRAY);
			reviewChargesTextView.setTextColor(Color.DKGRAY);

			weekdayRateTextView.setBackgroundColor(Color.WHITE);
			saturdayRateTextView.setBackgroundColor(Color.LTGRAY);
			reviewChargesTextView.setBackgroundColor(Color.LTGRAY);
		} else if (selectedSegmentedIndex == 1) {
			weekdayRateTextView.setTextColor(Color.DKGRAY);
			saturdayRateTextView.setTextColor(0xff85b8c7);
			reviewChargesTextView.setTextColor(Color.DKGRAY);

			weekdayRateTextView.setBackgroundColor(Color.LTGRAY);
			saturdayRateTextView.setBackgroundColor(Color.WHITE);
			reviewChargesTextView.setBackgroundColor(Color.LTGRAY);
		} else if (selectedSegmentedIndex == 2) {
			weekdayRateTextView.setTextColor(Color.DKGRAY);
			saturdayRateTextView.setTextColor(Color.DKGRAY);
			reviewChargesTextView.setTextColor(0xff85b8c7);

			weekdayRateTextView.setBackgroundColor(Color.LTGRAY);
			saturdayRateTextView.setBackgroundColor(Color.LTGRAY);
			reviewChargesTextView.setBackgroundColor(Color.WHITE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			TextView textView = (TextView) v;
			if (textView == weekdayRateTextView) {
				selectedSegmentedIndex = 0;
				callbacks.onWeekdayRateSelected();
			} else if (textView == saturdayRateTextView) {
				selectedSegmentedIndex = 1;
				callbacks.onSaturdayRateSelected();
			} else if (textView == reviewChargesTextView) {
				selectedSegmentedIndex = 2;
				callbacks.onReviewChangesRateSelected();
			}
			updateUI();
		}
	}

	public interface SegmentedControlFragmentCallbacks {
		void onWeekdayRateSelected();

		void onSaturdayRateSelected();

		void onReviewChangesRateSelected();
	}
}
