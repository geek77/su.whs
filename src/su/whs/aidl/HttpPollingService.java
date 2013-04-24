package su.whs.aidl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import su.whs.aidl.NameValuePair;
import su.whs.aidl.IOnHttpResponseListener;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class HttpPollingService extends Service {
	private static final String LOG_TAG = HttpPollingService.class.getName();
	
	private void handleGET(String url, List<NameValuePair> headers, List<NameValuePair> params, IOnHttpResponseListener callback) {
		Log.v(LOG_TAG,"handleGET('"+url+"')");
		HttpClient http = new DefaultHttpClient();
		String sparams = URLEncodedUtils.format(params, "utf-8");
		HttpGet request = new HttpGet(url + "?" + sparams);
		Log.v(LOG_TAG,"url = \""+request.getURI().toString() +"\"");
		for (NameValuePair arg :headers) {
			Log.v(LOG_TAG,"addHeader(\""+arg.getName() + "\", \"" + arg.getValue() + "\")");
			request.addHeader(arg.getName(), arg.getValue());
		}
		try {
			HttpResponse response = http.execute(request);
			HttpEntity entity = response.getEntity();
			int resultCode = response.getStatusLine().getStatusCode();
			Log.v(LOG_TAG,"handleGET() resultCode = " + String.valueOf(resultCode));
			callback.onHttpResponse(resultCode, EntityUtils.toString(entity));
			 
		} catch (ClientProtocolException e) {
			Log.e(LOG_TAG,"client protocol error");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOG_TAG,"io exception");
			e.printStackTrace();
		} catch (RemoteException e) {
			Log.e(LOG_TAG,"RemoteException:");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.e(LOG_TAG,"ParseException:");
			e.printStackTrace();
		}
		
	}
	
	private void handlePOST(String url, List<NameValuePair> headers, List<NameValuePair> params, List<NameValuePair> payload, IOnHttpResponseListener callback) {
		HttpClient http = new DefaultHttpClient();
		String sparams = URLEncodedUtils.format(params, "utf-8");
		HttpPost request = new HttpPost(url + "?" + sparams);
		for (NameValuePair arg :headers) {
			request.addHeader(arg.getName(), arg.getValue());
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(payload));
		} catch (UnsupportedEncodingException e1) {
			Log.e(LOG_TAG,"could not encode payload");			
		}
		try {
			HttpResponse response = http.execute(request);
			HttpEntity entity = response.getEntity();
			int resultCode = response.getStatusLine().getStatusCode();			
			callback.onHttpResponse(resultCode, EntityUtils.toString(entity));
		} catch (ClientProtocolException e) {
			Log.e(LOG_TAG,"client protocol error");
			// e.printStackTrace();
		} catch (IOException e) {
			// Log.e(LOG_TAG,"io exception");
			// e.printStackTrace();
		}  catch (RemoteException e) {
			Log.e(LOG_TAG,"RemoteException:");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.e(LOG_TAG,"ParseException:");
			e.printStackTrace();
		}
		
	}
		
	private final IHttpPollingService.Stub mBinder = new IHttpPollingService.Stub() {
		@SuppressWarnings("rawtypes")
		@Override
		public void get(final String url, final List headers, final List params,
				final IOnHttpResponseListener callback) throws RemoteException {
				AsyncTask<Void,Void,Void> as = new AsyncTask<Void,Void,Void>() {

					@SuppressWarnings("unchecked")
					@Override
					protected Void doInBackground(
							Void... arg0) {
						List<NameValuePair> Headers = (List<NameValuePair>)headers;
						List<NameValuePair> Params = (List<NameValuePair>)params;
						handleGET(url,Headers,Params,callback);
						return null;
					}
					
				};
				as.execute();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void post(final String url, final List headers, final List params, final List payload,
				final IOnHttpResponseListener callback) throws RemoteException {
			AsyncTask<Void,Void,Void> as = new AsyncTask<Void,Void,Void>() {

				@SuppressWarnings("unchecked")
				@Override
				protected Void doInBackground(
						Void... arg0) {					
					List<NameValuePair> Headers = (List<NameValuePair>)headers;
					List<NameValuePair> Params = (List<NameValuePair>)params;
					List<NameValuePair> Payload = (List<NameValuePair>)payload;
					handlePOST(url,Headers,Params,Payload,callback);
					return null;
				}
				
			};
			as.execute();
				
			
		}		
	};
	
	@Override
	public IBinder onBind(Intent intent) {	
		Log.v(LOG_TAG,"onBind()");
		return mBinder;
	}
	
	
}
