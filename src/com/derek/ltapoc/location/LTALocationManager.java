package com.derek.ltapoc.location;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class LTALocationManager {
	public static final String ACTION_LOCATION = "com.derek.lta.ACTION_LOCATION";
	private static LTALocationManager sLTALocationManager;
	private Context mAppContext;
	private LocationManager mLocationManager;

	private LTALocationManager(Context appContext) {
		mAppContext = appContext;
		mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
	}

	public static LTALocationManager get(Context c) {
		if (sLTALocationManager == null) {
			// Use the application context to avoid leaking activities
			sLTALocationManager = new LTALocationManager(c.getApplicationContext());
		}
		return sLTALocationManager;
	}

	private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
		Intent broadcast = new Intent(ACTION_LOCATION);
		int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
		return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
	}

	public void startLocationUpdates() {
		// String provider = LocationManager.GPS_PROVIDER;
		String provider = LocationManager.NETWORK_PROVIDER;
		// Start updates from the location manager
		PendingIntent pi = getLocationPendingIntent(true);
		mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
	}

	public void stopLocationUpdates() {
		PendingIntent pi = getLocationPendingIntent(false);
		if (pi != null) {
			mLocationManager.removeUpdates(pi);
			pi.cancel();
		}
	}

	public boolean isTrackingRun() {
		return getLocationPendingIntent(false) != null;
	}
}
