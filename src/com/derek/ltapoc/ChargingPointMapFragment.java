package com.derek.ltapoc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.derek.ltapoc.location.LTALocationManager;
import com.derek.ltapoc.location.LocationReceiver;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChargingPointMapFragment extends MapFragment {

	private GoogleMap mGoogleMap;
	private Location mLastLocation;
	private Marker mCurrentLocationMarker;

	private BroadcastReceiver mLocationReceiver = new LocationReceiver() {
		@Override
		protected void onLocationReceived(Context context, Location loc) {
			mLastLocation = loc;
			if (isVisible()) {
				updateUI();
			}
		}

		@Override
		protected void onProviderEnabledChanged(boolean enabled) {
			String toastText = enabled ? "GPS Enabled" : "GPS Disabled";
			Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);

		mGoogleMap = getMap();

		mGoogleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latlng) {
				Toast.makeText(getActivity(), "Latitude: " + latlng.latitude + " Longitude: " + latlng.longitude,
						Toast.LENGTH_SHORT).show();
			}
		});

		LTALocationManager.get(getActivity()).startLocationUpdates();

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		getActivity().registerReceiver(mLocationReceiver, new IntentFilter(LTALocationManager.ACTION_LOCATION));
	}

	@Override
	public void onStop() {
		LTALocationManager.get(getActivity()).stopLocationUpdates();
		getActivity().unregisterReceiver(mLocationReceiver);
		super.onStop();
	}

	private void updateUI() {

		if (mLastLocation != null && mGoogleMap != null) {
			LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

			if (mCurrentLocationMarker != null) {
				mCurrentLocationMarker.remove();
			}
			mCurrentLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(latlng).title("My Location"));

			CameraUpdate center = CameraUpdateFactory.newLatLng(latlng);
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
			mGoogleMap.moveCamera(center);
			mGoogleMap.animateCamera(zoom);
		}
	}
}
