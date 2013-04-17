package su.whs.ext;

import java.util.ArrayList;

import com.android.vending.billing.IInAppBillingService;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class BillingSupportService extends Service {
	IInAppBillingService mService;
	
	ServiceConnection mServiceConn = new ServiceConnection() {
		   @Override
		   public void onServiceDisconnected(ComponentName name) {
		       mService = null;
		       BillingSupportService.this.onBillingStop();
		   }

		   @Override
		   public void onServiceConnected(ComponentName name, 
		      IBinder service) {
		       mService = IInAppBillingService.Stub.asInterface(service);
		       Log.v("INFO","billing service connected");
		       BillingSupportService.this.onBillingStart();
		   }
		};

	protected void onBillingStart() {
		
	}
	
	protected void onBillingStop() {
		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub		
		return null;
	}	
	public int onStartCommand(Intent intent, int flags, int startId) {		
		bindService(new 
		        Intent("com.android.vending.billing.InAppBillingService.BIND"),
		                mServiceConn, Context.BIND_AUTO_CREATE);
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void onDestroy() {
		if (mServiceConn!=null)
			unbindService(mServiceConn);
		super.onDestroy();
	}
	
	protected ArrayList<String> loadPurchasedItems(String packageName) {
		return BillingHelper.loadPurchasedItems(mService, packageName);		
	}

	protected boolean isSkuPurchased(String packageName, String sku) {
		ArrayList<String> skus = loadPurchasedItems(packageName);
		Log.v("INFO","check if '" + sku + "' purchased ("+skus+")");
		if (skus!=null&&skus.size()>0&&skus.contains(sku)) return true;
		return false;
	}
}
