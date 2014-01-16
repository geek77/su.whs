package su.whs.forms;

import java.io.IOException;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParserException;

import su.whs.utils.PreferenceItemsFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class PreferencesActivity extends FragmentActivity {
	private static final String LOG_TAG = PreferencesActivity.class.getName();
	
	private Stack<String> mFakeBackwardStack = new Stack<String>();
	// private String mCurrentScreenName = "ROOT";
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			new PreferenceItemsFactory(this.getApplicationContext());
		} catch (XmlPullParserException e) {
			Log.e(LOG_TAG,"could not read configuration description xml");
			return;
		} catch (IOException e) {
			Log.e(LOG_TAG,"i/o error");
			return;
		}
		
		
	}
	
	@Override
	public void onDestroy() {
		
	}
	
	@Override
	public void onBackPressed() {
		if (mFakeBackwardStack.empty())
			super.onBackPressed();
		showScreen(mFakeBackwardStack.pop());
	}
	
	private void showScreen(String name) {
		
	}
	
	/*
	private String getCurrentScreenName() {
		return mCurrentScreenName;
	} */
}
