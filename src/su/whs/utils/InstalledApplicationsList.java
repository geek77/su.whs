package su.whs.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class InstalledApplicationsList {
	SharedPreferences cfg = null;
	Context ctx = null;
	
	public InstalledApplicationsList(Context context,SharedPreferences prefs) {
		cfg = prefs;
		ctx = context;
	}
	
	public List<InstalledApplicationModel> all(String realm) {
		ArrayList<InstalledApplicationModel> result = new ArrayList<InstalledApplicationModel>();
		String conf = cfg.getString(realm, "");
		if (!conf.equals("")) {
			JSONObject json = null;
			try {
				json = new JSONObject(conf);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("CRITICAL","could not parse json parameter ");
				e.printStackTrace();				
			};
			if (json!=null && ctx!=null) {
				PackageManager pm = ctx.getPackageManager();
				Intent intent = new Intent(Intent.ACTION_MAIN, null);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
				for (ResolveInfo rInfo :list) {
					String name = rInfo.activityInfo.packageName;
					JSONObject app = null;
					InstalledApplicationModel model = new InstalledApplicationModel(name,rInfo.activityInfo.loadIcon(pm),rInfo.activityInfo.loadLabel(pm).toString());
					
					try {
						 app = json.getJSONObject(name);
					} catch (JSONException e) {
											
					}
					if (app!=null) {
						// mark this applicationModel flagged as configured
						model.addFlag(1);
					}
					result.add(model);
				}
			}
		}
		
		return result;
	}
	
	public void Update(String realm, InstalledApplicationModel model) {
		String conf = cfg.getString(realm, "");
		if (conf.equals("")) {
			conf = "{}";
		};
		JSONObject json = null;
		try {
			json = new JSONObject(conf);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("CRITICAL","could not parse json parameter ");
							
		};
		String val = null;
		try {
			val = json.getString(model.getPackageName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("ERROR","could not read json for " + model.getPackageName());		
		}	
		if (val!=null && json!=null) {
			if (!model.isSelected()) {
				json.remove(model.getPackageName());
			}
		} else if (val==null && model.isSelected()) {
			try {
				json.put(model.getPackageName(), 1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.v("ERROR","Could not save model to json");
			}
		}
		SharedPreferences.Editor e = cfg.edit();
		e.putString(realm,json.toString());
		e.commit();
	}
}
