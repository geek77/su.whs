package su.whs.forms;

import su.whs.R;
import su.whs.widgets.PincodeBox;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class PincodeProtectedActivity extends FragmentActivity {
	private static final String LOG_CAT = PincodeProtectedActivity.class.getName();
	private FrameLayout mRootView;
	private View mContentView;
	private PincodeBox mPincodeBox;
	private boolean isContentHidden = false;
	
	public void setPincode(String pincode) {
		mPincodeBox.setExpectedPincode(pincode);
	}
	
	public void SetPassword(String password) {
		mPincodeBox.setExpectedPincode(password);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.pincode_protected_layout);
		mRootView = (FrameLayout) super.findViewById(R.id.pincodeProtectedRoot);		
		FragmentManager fm = this.getSupportFragmentManager();
		mPincodeBox = (PincodeBox) fm.findFragmentById(R.id.pincodeBox);
		PackageManager pm = getPackageManager();
		ApplicationInfo ai;
		try {
			ai = pm.getApplicationInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			return;
		}
		Drawable icon = pm.getApplicationIcon(ai);
		mPincodeBox.setLabel(pm.getApplicationLabel(ai).toString(), icon);
	}
	
	
	public void setContentView(View v) {
		mContentView = v;
		mRootView.addView(v,0);
	}
	
	public void setContentView(int id) {
		LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		setContentView(li.inflate(id,null));
	}
		
	public View findViewById(int id) {
		return mContentView.findViewById(id);
	}
	
	protected boolean isSelfLockMode() {
		return false;
	}
	
	protected String getExpectedPassword() {
		return "0000";
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isSelfLockMode()) {
			showPincodeBox();
		} else {
			showContent();
		}
	}
	
	protected void showPincodeBox() {
		isContentHidden = true;
		// if (mContentView!=null)
		// 	mContentView.setVisibility(View.GONE);
		mPincodeBox.getView().setVisibility(View.VISIBLE);		
	}
	
	protected void showContent() {
		isContentHidden = false;
		mPincodeBox.getView().setVisibility(View.GONE);
		// if (mContentView!=null) 
		//	mContentView.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d(LOG_CAT,"dispatchKeyEvent: " + event);
		if (isContentHidden) {
			mPincodeBox.dispatchKeyEvent(event);
		}
		return super.dispatchKeyEvent(event);
	}

	protected int getIcon() {
		return android.R.drawable.sym_def_app_icon;
	}
	
}
