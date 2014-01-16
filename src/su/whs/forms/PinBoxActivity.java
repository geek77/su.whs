package su.whs.forms;

import su.whs.R;
import su.whs.widgets.PincodeBox;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public abstract class PinBoxActivity extends FragmentActivity implements PincodeBox.OnPincodeInputListener {
	private static final String TAG = "PinBoxActivity";
	protected boolean isPasswordEntered = false;
	private PincodeBox mPincodeBox;
	private String mLabel = null;
	private Drawable mIcon = null;
	
	protected void designThis() {
		mPincodeBox.setLabel(mLabel, mIcon);
	}
	
	protected void configureThis(Bundle cfg) {
		if (cfg.containsKey("package")) {
			PackageInfo pkg = (PackageInfo) cfg.get("package");
			String packageName = pkg.packageName;
			PackageManager pm = getPackageManager();
			ApplicationInfo ai;
			try {
				ai = pm.getApplicationInfo(packageName, 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				return;
			}
			mLabel = pm.getApplicationLabel(ai).toString();
			mIcon = pm.getApplicationIcon(ai);
		}
	}
		
	@SuppressWarnings("deprecation")
	@Override 
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		} else {
			WindowManager.LayoutParams lp = getWindow().getAttributes();  
			lp.dimAmount=0.5f;
			getWindow().setAttributes(lp);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			getWindow().setBackgroundDrawable(new ColorDrawable(0x7000000));
		}
		setContentView(R.layout.pincode_activity);
		FragmentManager fm = getSupportFragmentManager();
		mPincodeBox = (PincodeBox) fm.findFragmentById(R.id.fragment1);
		mPincodeBox.clear();
		isPasswordEntered = false;
		configureThis(getIntent().getExtras());
		designThis();
	}
	
	@Override
	public void onSaveInstanceState(Bundle sis) {
		sis.putString("label", mLabel);
	}
	
	@Override
	public void onCorrectPincode() {
		isPasswordEntered = true;
		finish();
	}

	@Override
	public void onIncorrectPincode() {
		isPasswordEntered = false;
	}
	
	
	@Override
	public void onDestroy() {
		Log.v(TAG,"onDestroy()");
		super.onDestroy();
	}
	
	@Override
	public final void onBackPressed() {
		onCancel();
	}
	
	public final void clear() {
		mPincodeBox.clear();
	}
	
	public abstract void onCancel();
}
