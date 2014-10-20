package com.derek.ltapoc;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;

public class ChargingPointActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charging_point);

		if (savedInstanceState == null) {
			Builder builder = new CameraPosition.Builder();
			builder.target(new LatLng(1, 103));
			builder.zoom(13);
			CameraPosition cp = builder.build();

			ChargingPointMapFragment mapFragment = new ChargingPointMapFragment();

			getFragmentManager().beginTransaction()
					.add(R.id.container_charging_point_tools, new ChargingPointToolsFragment()).commit();
			getFragmentManager().beginTransaction().add(R.id.container_charging_point, mapFragment).commit();
		}
	}
}
