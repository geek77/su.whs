package su.whs.ext;

import su.whs.utils.CircularBuffer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationTrackingService extends Service {	
	public static final String LOG_TAG = LocationTrackingService.class.getName();
	
	private CircularBuffer<Location> mLocationsRing = new CircularBuffer<Location>(10);
	private int mMinimalDistance = 100;
	private int mMinimalIntervalSeconds = 60;
	
	public interface OnLocationChangedListener {
		void onLocationChanged(Location loc);
	}
	
	private OnLocationChangedListener mLocationChangedListener = null;
	private LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			synchronized(mLocationsRing) { 
				mLocationsRing.insert(location);			
			}
			if (mLocationChangedListener!=null)
				mLocationChangedListener.onLocationChanged(location);
		}

		@Override
		public void onProviderDisabled(String provider) {			
			
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			
		}
		
	};		
	
	public LocationTrackingService() {		
	}
	
	public LocationTrackingService(int minDist, int minInt) {
		mMinimalDistance = minDist;
		mMinimalIntervalSeconds = minInt;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public void onCreate() {
		Log.d(LOG_TAG,"onCreate()");
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false); // does not require direction 
		c.setCostAllowed(false);
		c.setPowerRequirement(Criteria.POWER_LOW); 
		String locationProvider = lm.getBestProvider(c, true);
		lm.requestLocationUpdates(locationProvider, mMinimalIntervalSeconds * 1000 , mMinimalDistance, mLocationListener);
	}
	
	public void onDestroy() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(mLocationListener);
	}
	
	public void setOnLocationChangedListener(OnLocationChangedListener l) {
		mLocationChangedListener = l;
	}
	
	public void removeOnLocationChangedListener(OnLocationChangedListener l) {
		mLocationChangedListener = null;
	}
	
	public Location getLastKnownLocation() {
		Location result = null;
		synchronized(mLocationsRing) {
			result = mLocationsRing.getNewest();
		}
		return result;
	}
}
