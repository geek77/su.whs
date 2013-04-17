package su.whs.net;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkState {
	
	public static boolean isNetworkAvailable(Context context) {
		String cs = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(cs);
		if (cm.getActiveNetworkInfo()==null)
			return false;
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

}
