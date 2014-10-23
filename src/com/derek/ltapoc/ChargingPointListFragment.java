package com.derek.ltapoc;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ChargingPointListFragment extends Fragment {
	private ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_charging_point_list, container, false);

		mListView = (ListView) rootView.findViewById(R.id.list_view_charging_point);

		return rootView;
	}
}
