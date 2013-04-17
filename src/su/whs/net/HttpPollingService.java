package su.whs.net;

import su.whs.aidl.IHttpPollingService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HttpPollingService extends Service {
	
	private final IHttpPollingService.Stub mBinder = new IHttpPollingService.Stub() {
		
	};
	
	@Override
	public IBinder onBind(Intent intent) {		
		return mBinder;
	}
}
