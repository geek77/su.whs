package su.whs.forms;

import su.whs.R;
import su.whs.widgets.PincodeBox;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class PinBoxActivity extends FragmentActivity implements PincodeBox.OnPincodeInputListener {
	
	protected boolean isPasswordEntered = false;
	private PincodeBox mPincodeBox;
	
	protected void designThis() {
		
	}
	
	protected void configureThis(Bundle cfg) {
		
	}
		
	@Override 
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
