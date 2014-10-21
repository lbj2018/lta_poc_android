package com.derek.ltapoc;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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
import com.derek.ltapoc.model.ChargingPoint;
import com.derek.ltapoc.model.ChargingPointDataSource;
import com.derek.ltapoc.model.LTADataStore;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChargingPointMapFragment extends MapFragment {

	private GoogleMap mGoogleMap;
	private Location mLastLocation;
	private boolean mIsFirstGetLocation = true;
	// private Marker mCurrentLocationMarker;
	private ArrayList<Marker> mChargingPointMarkers;
	private ArrayList<ChargingPoint> mChargingPoints;

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

		mChargingPointMarkers = new ArrayList<Marker>();

		ChargingPointDataSource dataSource = new ChargingPointDataSource(getActivity());
		dataSource.open();
		ArrayList<ChargingPoint> points = dataSource.getAllChargingPoints();
		dataSource.close();

		if (points != null) {
			LTADataStore.get().setChargingPoints(points);
		}
		mChargingPoints = LTADataStore.get().getChargingPoints();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);

		mGoogleMap = getMap();

		if (mGoogleMap != null) {
			mGoogleMap.setMyLocationEnabled(true);

			for (ChargingPoint aPoint : mChargingPoints) {
				LatLng latlng = new LatLng(aPoint.getLatitude(), aPoint.getLongitude());
				Marker chargingPointMarker = mGoogleMap.addMarker(new MarkerOptions().position(latlng).title(
						aPoint.getChargingPointId()));
				mChargingPointMarkers.add(chargingPointMarker);
			}
		}

		mGoogleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latlng) {
				ChargingPoint chargingPoint = new ChargingPoint();
				chargingPoint.setChargingPointId(UUID.randomUUID().toString());
				chargingPoint.setLatitude(latlng.latitude);
				chargingPoint.setLongitude(latlng.longitude);
				chargingPoint.setRoadName("");
				chargingPoint.setRateTemplateId("");
				chargingPoint.setEffectiveDate(new Date());

				// save into database
				ChargingPointDataSource dataSource = new ChargingPointDataSource(getActivity());
				dataSource.open();
				dataSource.createChargingPoint(chargingPoint);
				dataSource.close();

				LTADataStore.get().getChargingPoints().add(chargingPoint);

				Marker chargingPointMarker = mGoogleMap.addMarker(new MarkerOptions().position(latlng).title(
						chargingPoint.getChargingPointId()));
				mChargingPointMarkers.add(chargingPointMarker);

				Toast.makeText(getActivity(), "Latitude: " + latlng.latitude + " Longitude: " + latlng.longitude,
						Toast.LENGTH_SHORT).show();
			}
		});

		mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				String chargingPointId = marker.getTitle();

				EditChargingPointDialogFragment dialogFragment = new EditChargingPointDialogFragment();
				Bundle arguments = new Bundle();
				arguments.putString(EditChargingPointDialogFragment.CHARGING_POINT_ID, chargingPointId);
				dialogFragment.setArguments(arguments);
				dialogFragment.show(getActivity().getFragmentManager(), "EditChargingPointDialogFragment");

				return true;
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

			// if (mCurrentLocationMarker != null) {
			// mCurrentLocationMarker.remove();
			// }
			// mCurrentLocationMarker = mGoogleMap.addMarker(new
			// MarkerOptions().position(latlng).title("My Location"));

			if (mIsFirstGetLocation) {
				CameraUpdate center = CameraUpdateFactory.newLatLng(latlng);
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
				mGoogleMap.moveCamera(center);
				mGoogleMap.animateCamera(zoom);

				mIsFirstGetLocation = false;
			}
		}
	}
}
