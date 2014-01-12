package su.whs.utils;

import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;

public class PreferenceSwitcher extends PreferenceItem {
	private static final String LOG_TAG = PreferenceSwitcher.class.getName();
	private String mSummary;
	private String mSummaryOn;
	private String mSummaryOff;
	protected boolean mChecked;
	
	public PreferenceSwitcher(String optName) {
		super(optName);
	}

	public PreferenceSwitcher(String optName, PreferenceItem parent) {
		super(optName, parent);
	}

	public PreferenceSwitcher(AttributeSet attr, PreferenceItem parent) {
		super(attr,parent);
		String summary = attr.getAttributeValue("android", "summary");
		String summaryOn = attr.getAttributeValue("android", "summaryOn");
		String summaryOff = attr.getAttributeValue("android", "summaryOff");
		boolean checked = attr.getAttributeBooleanValue("android", "checked", false);
		init(summary,summaryOn,summaryOff,checked);
	}
	
	private void init(String sum, String sumOn, String sumOff, boolean checked) {
		Log.d(LOG_TAG,"name = " + getName() + ",  summary = " + sum + ", summaryOn = " + sumOn + ", summaryOff = " + sumOff + ", checked = " + String.valueOf(checked));
		mSummary = sum;
		mSummaryOn = sumOn;
		mSummaryOff = sumOff;
		mChecked = checked;
	}
	
	public boolean isChecked() { return mChecked; }
	public String getSummary() {
		if (mChecked) {
			if (mSummaryOn!=null)
				return mSummaryOn;			
		} else {
			if (mSummaryOff!=null) 
				return mSummaryOff;			
		}
		return mSummary;
	}
	
	public void store(SharedPreferences.Editor spe) {
		Log.d(LOG_TAG,"store " + getName() + " = " + String.valueOf(mChecked));
		super.store(spe);
		spe.putBoolean(getName(), mChecked);
	}
	
	public void setChecked(boolean checked) {
		mChecked = checked;
	}
}
