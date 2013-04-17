package su.whs.forms;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import su.whs.ext.BillingHelper;

import com.android.vending.billing.IInAppBillingService;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class BillingSupportFragmentActivity extends Activity {
	IInAppBillingService mService;
	boolean billingStarted = false;
	ServiceConnection mServiceConn = new ServiceConnection() {
		   @Override
		   public void onServiceDisconnected(ComponentName name) {
		       mService = null;
		       billingStarted = false;
		       onBillingStop();
		   }

		   @Override
		   public void onServiceConnected(ComponentName name, 
		      IBinder service) {
		       mService = IInAppBillingService.Stub.asInterface(service);
		       billingStarted = true;
		       onBillingStart();
		   }
		};
		
	protected boolean isBillingStarted() {
		return billingStarted;
	}
	protected void onBillingStart() {
		
	}
	
	protected void onBillingStop() {
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindService(new 
		        Intent("com.android.vending.billing.InAppBillingService.BIND"),
		                mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	protected ArrayList<String> loadPurchasedItems(String packageName) {
		return BillingHelper.loadPurchasedItems(mService, packageName);		
	}
	
	protected void startBuySku(String sku) {
		Bundle buyIntentBundle = null;
		try {
			 buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
					   sku, "inapp", "---random--payload--");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;	
		}
		
		int response = buyIntentBundle.getInt("RESPONSE_CODE");
		if (response == 0) {
			PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
			try {
				startIntentSenderForResult(pendingIntent.getIntentSender(),
						   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
						   Integer.valueOf(0));
				
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void OnPurchaseSuccess(String sku) {
		
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	   if (requestCode == 1001) {           
	      // int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	      
	      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	      // String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
	      
	      if (resultCode == RESULT_OK) {
	         try {
	            JSONObject jo = new JSONObject(purchaseData);
	            String sku = jo.getString("productId");
	            OnPurchaseSuccess(sku);
	            // alert("You have bought the " + sku + ". Excellent choice, adventurer!");
	          }
	          catch (JSONException e) {
	             // alert("Failed to parse purchase data.");
	             e.printStackTrace();
	          }
	      }
	   }
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	    if (mServiceConn != null) {
	        unbindService(mServiceConn);
	    }   		
	}
}
