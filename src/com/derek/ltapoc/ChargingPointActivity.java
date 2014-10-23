package com.derek.ltapoc;

import android.app.Activity;
import android.os.Bundle;

public class ChargingPointActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charging_point);

		if (savedInstanceState == null) {
			ChargingPointMapFragment mapFragment = new ChargingPointMapFragment();

			getFragmentManager().beginTransaction()
					.add(R.id.container_charging_point_tools, new ChargingPointToolsFragment()).commit();
			getFragmentManager().beginTransaction().add(R.id.container_charging_point, mapFragment).commit();
		}
	}
}
