package su.whs.utils;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import su.whs.ext.BillingHelper;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

public class BillingSupport {
	public interface BillingSupportListener {
		void onBillingStart();
		void onBillingStop();		
		void OnPurchaseSuccess(String sku);
	}
	
	private Context mContext;
	private IInAppBillingService mService;
	private boolean billingStarted = false;
	private BillingSupportListener mListener;
	
	ServiceConnection mServiceConn = new ServiceConnection() {
		   @Override
		   public void onServiceDisconnected(ComponentName name) {
		       mService = null;
		       billingStarted = false;
		       mListener.onBillingStop();
		   }

		   @Override
		   public void onServiceConnected(ComponentName name, 
		      IBinder service) {
		       mService = IInAppBillingService.Stub.asInterface(service);
		       billingStarted = true;
		       mListener.onBillingStart();
		   }
		};
		
	public boolean isBillingStarted() {
		return billingStarted;
	}
	
	
	/* public */
	public BillingSupport(Context context, BillingSupportListener l) {
		mListener = l;
		context.bindService(new 
		        Intent("com.android.vending.billing.InAppBillingService.BIND"),
                mServiceConn, Context.BIND_AUTO_CREATE);
		mContext = context;
	}
	
	public void onDestroy() {
		mContext.unbindService(mServiceConn);
	}
	
	public ArrayList<String> loadPurchasedItems(String packageName) {
		return BillingHelper.loadPurchasedItems(mService, packageName);		
	}
	
	protected void startBuySku(String sku) {
		Bundle buyIntentBundle = null;
		try {
			 buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(),
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
				if (mContext instanceof Activity) {
					
				
					((Activity)mContext).startIntentSenderForResult(pendingIntent.getIntentSender(),
						   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
						   Integer.valueOf(0));
				}
				
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		   if (requestCode == 1001) {           
		      // int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
		      
		      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
		      // String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
		      
		      if (resultCode == Activity.RESULT_OK) {
		         try {
		            JSONObject jo = new JSONObject(purchaseData);
		            String sku = jo.getString("productId");
		            mListener.OnPurchaseSuccess(sku);
		            // alert("You have bought the " + sku + ". Excellent choice, adventurer!");
		          }
		          catch (JSONException e) {
		             // alert("Failed to parse purchase data.");
		             e.printStackTrace();
		          }
		      }
		 }
	}
}
