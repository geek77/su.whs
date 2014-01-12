package su.whs.forms;

import su.whs.R;
import su.whs.widgets.PincodeBox;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;

public class PinBoxActivity extends FragmentActivity implements PincodeBox.OnPincodeInputListener {
	
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
		
	@Override 
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.pincode_activity);
		FragmentManager fm = getSupportFragmentManager();
		mPincodeBox = (PincodeBox) fm.findFragmentById(R.id.fragment1);
		configureThis(getIntent().getExtras());
		designThis();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mPincodeBox.clear();
		isPasswordEntered = false;
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
}
