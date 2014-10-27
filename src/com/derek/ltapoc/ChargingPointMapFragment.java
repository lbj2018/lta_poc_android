package com.derek.ltapoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.derek.ltapoc.EditChargingPointDialogFragment.EditChargingPointDialogFragmentCallbacks;
import com.derek.ltapoc.location.LTALocationManager;
import com.derek.ltapoc.location.LocationReceiver;
import com.derek.ltapoc.model.ChargingPoint;
import com.derek.ltapoc.model.ChargingPointDataSource;
import com.derek.ltapoc.model.LTADataStore;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChargingPointMapFragment extends MapFragment implements EditChargingPointDialogFragmentCallbacks {
	private static final double CHARGING_POINT_RADIUS = 500;
	private GoogleMap mGoogleMap;
	private Location mLastLocation;
	private boolean mIsFirstGetLocation = true;
	private ArrayList<ChargingPoint> mChargingPoints;
	private HashMap<String, Marker> mChargingPointMarkers;
	private ProgressDialog mProgressDialog;

	private BroadcastReceiver mLocationReceiver = new LocationReceiver() {
		@Override
		protected void onLocationReceived(Context context, Location loc) {
			mLastLocation = loc;
			if (isVisible()) {
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
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

		mChargingPointMarkers = new HashMap<String, Marker>();

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
						aPoint.getRoadName()));
				mChargingPointMarkers.put(aPoint.getChargingPointId(), chargingPointMarker);

				circleLacation(latlng);
			}
		}

		mGoogleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latlng) {

				new GetAddressTask().execute(latlng);
			}
		});

		mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				String chargingPointId = null;

				for (Map.Entry<String, Marker> entry : mChargingPointMarkers.entrySet()) {
					Marker aMarker = entry.getValue();
					if (aMarker.equals(marker)) {
						chargingPointId = entry.getKey();
						break;
					}
				}

				ChargingPoint chargingPoint = LTADataStore.get().getChargingPoint(chargingPointId);

				if (chargingPoint != null) {
					EditChargingPointDialogFragment dialogFragment = new EditChargingPointDialogFragment();
					dialogFragment.setCallbacks(ChargingPointMapFragment.this);
					dialogFragment.setChargingPoint(chargingPoint);
					dialogFragment.show(getActivity().getFragmentManager(), "EditChargingPointDialogFragment");
				}
			}
		});

		LTALocationManager.get(getActivity()).startLocationUpdates();

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();

		mProgressDialog = ProgressDialog.show(getActivity(), "", "loading your location...");
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

			if (mIsFirstGetLocation) {
				CameraUpdate center = CameraUpdateFactory.newLatLng(latlng);
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
				mGoogleMap.moveCamera(center);
				mGoogleMap.animateCamera(zoom);

				mIsFirstGetLocation = false;
			}
		}
	}

	private void circleLacation(LatLng latlng) {
		CircleOptions options = new CircleOptions();
		options.center(latlng);
		options.radius(CHARGING_POINT_RADIUS);
		options.strokeColor(0xffff0000);
		options.strokeWidth(3);
		options.visible(true);
		mGoogleMap.addCircle(options);
	}

	private class GetAddressTask extends AsyncTask<LatLng, Void, String> {
		private ProgressDialog mProgressDialog;
		private double mLatitude;
		private double mLongitude;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressDialog = ProgressDialog.show(getActivity(), "", "Loading your address by your coordinate...");
		}

		@Override
		protected String doInBackground(LatLng... params) {
			Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

			LatLng latlng = params[0];

			mLatitude = latlng.latitude;
			mLongitude = latlng.longitude;

			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
			} catch (IOException e1) {
				Log.e("GetAddressTask", "IO Exception in getFromLocation()");
				e1.printStackTrace();
				return "";
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments " + Double.toString(mLatitude) + " , "
						+ Double.toString(mLongitude) + " passed to address service";
				Log.e("GetAddressTask", errorString);
				e2.printStackTrace();
				return "";
			}
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				String addressText = String.format("%s, %s, %s",
				// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
						// Locality is usually a city
						address.getLocality(),
						// The country of the address
						address.getCountryName());
				// Return the text

				String thoroughfare = address.getThoroughfare();

				return thoroughfare;
			} else {
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();

			ChargingPoint chargingPoint = new ChargingPoint();
			chargingPoint.setChargingPointId(UUID.randomUUID().toString());
			chargingPoint.setLatitude(mLatitude);
			chargingPoint.setLongitude(mLongitude);
			chargingPoint.setRoadName(result);
			chargingPoint.setRateTemplateId("");
			chargingPoint.setEffectiveDate(new Date());

			EditChargingPointDialogFragment dialogFragment = new EditChargingPointDialogFragment();
			dialogFragment.setCallbacks(ChargingPointMapFragment.this);
			dialogFragment.setChargingPoint(chargingPoint);
			dialogFragment.show(getActivity().getFragmentManager(), "EditChargingPointDialogFragment");
		}
	}

	@Override
	public void onDidSaveChargingPoint(ChargingPoint point) {
		// save into database
		if (!LTADataStore.get().getChargingPoints().contains(point)) {
			ChargingPointDataSource dataSource = new ChargingPointDataSource(getActivity());
			dataSource.open();
			dataSource.createChargingPoint(point);
			dataSource.close();

			LTADataStore.get().getChargingPoints().add(point);

			LatLng latlng = new LatLng(point.getLatitude(), point.getLongitude());
			Marker chargingPointMarker = mGoogleMap.addMarker(new MarkerOptions().position(latlng).title(
					point.getRoadName()));

			mChargingPointMarkers.put(point.getChargingPointId(), chargingPointMarker);

			chargingPointMarker.showInfoWindow();

			circleLacation(latlng);
		} else {
			ChargingPointDataSource dataSource = new ChargingPointDataSource(getActivity());
			dataSource.open();
			dataSource.updateChargingPoint(point);
			dataSource.close();
		}
	}

	@Override
	public void onDidCancelChargingPoint(ChargingPoint point) {
	}
}
