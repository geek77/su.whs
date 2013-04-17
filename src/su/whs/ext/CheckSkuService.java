package su.whs.ext;

import com.android.vending.billing.IInAppBillingService;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class CheckSkuService extends Service {

	IInAppBillingService mService;
	
	ServiceConnection mServiceConn = new ServiceConnection() {
		   @Override
		   public void onServiceDisconnected(ComponentName name) {
		       mService = null;
		   }

		   @Override
		   public void onServiceConnected(ComponentName name, 
		      IBinder service) {
		       mService = IInAppBillingService.Stub.asInterface(service);
		       // now we can check sku !
		   }
		};
		
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		bindService(new 
		        Intent("com.android.vending.billing.InAppBillingService.BIND"),
		                mServiceConn, Context.BIND_AUTO_CREATE);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	    if (mServiceConn != null) {
	        unbindService(mServiceConn);
	    }   
	}

}
