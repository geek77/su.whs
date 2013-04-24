package su.whs.net;

import java.util.List;

import su.whs.aidl.HttpPollingService;
import su.whs.aidl.IHttpPollingService;
import su.whs.aidl.IOnHttpResponseListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import su.whs.aidl.NameValuePair;

public class AsyncHttp {
	private static final String LOG_TAG = AsyncHttp.class.getName();
	
	public interface OnHttpResponseListener {
		void onHttpResponse(int resultCode, String resultBody);
	}
	
	private IHttpPollingService mHttp;
	private boolean mServiceConnected = false;
	private Object mLock = new Object();
	private Context mContext;
	
	private ServiceConnection mServiceConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v(LOG_TAG,"onServiceConnected()");
			mHttp = IHttpPollingService.Stub.asInterface(service);
			synchronized(mLock) {
				mServiceConnected = true;
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.v(LOG_TAG,"onServiceDisconnected");
			mHttp = null;	
			synchronized(mLock) {
				mServiceConnected = false;
			}
		}
		
	};
	
	public AsyncHttp(Context context) {
		Log.v(LOG_TAG,"try to bind to service");
		context.bindService(new Intent(context,HttpPollingService.class), mServiceConn, Context.BIND_AUTO_CREATE);
		mContext = context;
	}
	
	public void onDestroy() {
		mContext.unbindService(mServiceConn);
	}
	
	private boolean waitForService() {
		int counter = 0;
		do {
			synchronized(mLock) {
				if (mServiceConnected) return true;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					return false;
				}
				counter++;
				if (counter>50) return false;
			}
		} while(true);
	}
	
	/* debug helper */
	private void LogNVP(String tag, List<NameValuePair> l) {
		for (NameValuePair nvp :l) {
			Log.v(LOG_TAG,tag + ": " + nvp.getName() + " = \"" + nvp.getValue() + "\"");
		}
	}
	
	/* public methods */
	
	public void GET(String url, List<NameValuePair> headers, List<NameValuePair> params, final OnHttpResponseListener callback) {
		if (!waitForService()) {
			Log.e(LOG_TAG,"HttpPollingService not ready yet.");
			callback.onHttpResponse(503, " {\"error\" : \"could not connect to service\" } ");
			return;
		}
		LogNVP("header",headers);
		LogNVP("param",params);
		try {
			Log.v(LOG_TAG,"GET('"+url+"')");
			mHttp.get(url, headers, params, new IOnHttpResponseListener.Stub() {
				
				@Override
				public void onHttpResponse(int resultCode, String resultBody)
						throws RemoteException {
					callback.onHttpResponse(resultCode, resultBody);
					
				}
			});
		} catch (RemoteException e) {
			Log.e(LOG_TAG,"RemoteException: " + e);
			callback.onHttpResponse(503, " {\"error\" : \"internal AsyncHttp error\" }");
		}	
	}
	
	public void POST(String url, List<NameValuePair> headers, List<NameValuePair> params, List<NameValuePair> payload, final OnHttpResponseListener callback) {
		if (!waitForService()) {
			Log.e(LOG_TAG,"HttpPollingService not ready yet.");
			callback.onHttpResponse(503, " {\"error\" : \"could not connect to service\" } ");
			return;
		}
		try {
			mHttp.post(url, headers, params, payload, new IOnHttpResponseListener.Stub() {			
				@Override
				public void onHttpResponse(int resultCode, String resultBody)
						throws RemoteException {
					callback.onHttpResponse(resultCode, resultBody);
				}
			});
		} catch (RemoteException e) {
			callback.onHttpResponse(503, " {\"error\" : \"internal AsyncHttp error\" }");
		}
			
	}
}