package su.whs.ext;

import java.util.ArrayList;

import com.android.vending.billing.IInAppBillingService;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public class BillingHelper {
	public static ArrayList<String> loadPurchasedItems(IInAppBillingService mService, String packageName) {
		if (mService==null) return null;
		Bundle ownedItems = null;
		ArrayList<String> result = new ArrayList<String>();
		String continuationToken = null;
		do {
			try {
				ownedItems = mService.getPurchases(3, packageName, "inapp", null);
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;				
			}
			
			int response = ownedItems.getInt("RESPONSE_CODE");
			if (response == 0) {
				ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
				/*
				ArrayList purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
				ArrayList signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
				*/
				continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");
				
				for (int i=0; i<ownedSkus.size(); i++) {
					result.add(ownedSkus.get(i));
					Log.v("INFO", "purchased sku: " + ownedSkus.get(i));
				}
			};
		}  while(continuationToken!=null);
		return result;
	}
}
