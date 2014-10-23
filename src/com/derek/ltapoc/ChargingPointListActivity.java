package com.derek.ltapoc;

import android.app.Activity;
import android.os.Bundle;

public class ChargingPointListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charging_point_list);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new ChargingPointListFragment()).commit();
		}
	}
}
