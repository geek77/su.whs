package su.whs.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PreferenceItem {
	private static final String LOG_TAG = PreferenceItem.class.getName();
	private List<PreferenceItem> mChilds = null;
	private Map<String,String> mDependencies = new HashMap<String,String>();
	private String mOptionName = "";
	private String mOptionTitle = null;
	private PreferenceItem mParent = null;
	private boolean mEnabled = true;
	private OnPreferenceChangedListener mOnPreferenceChangedListener;
	private OnPreferenceDependencyStateListener mOnDependencyStateListener;
	
	public interface OnPreferenceChangedListener {
		void onPreferenceChanged(PreferenceItem item);
	}
	
	public interface OnPreferenceDependencyStateListener {
		void onPreferenceStateChanged(PreferenceItem item);
	}
	
	public PreferenceItem(String optName, PreferenceItem parent) {
		init(optName,parent);
	}
	
	public PreferenceItem(String optName) {
		init(optName,null);
	}
	
	public PreferenceItem(AttributeSet attr, PreferenceItem parent) {
		
		String optName = attr.getAttributeValue("android", "name");
		String optTitle = attr.getAttributeValue("android", "text");
		mEnabled = attr.getAttributeBooleanValue("android", "enabled", true);
		Log.d(LOG_TAG,"Name = " + optName + ", Title = " + optTitle);
		init(optName,parent);
		mOptionTitle = optTitle;
	}
	
	private void init(String optName, PreferenceItem parent) {
		Log.d(LOG_TAG,"");
		mChilds = new ArrayList<PreferenceItem>();
		mOptionName = optName;
		mParent = parent;
	}
	
	public void notifyChildValueChanged(PreferenceItem child) {
		for(PreferenceItem ch :mChilds) {
			if (ch==child) continue;
			ch.checkForDependState(ch);
		}
	}
	
	public void checkForDependState(PreferenceItem i ) {
		return ;
	}
		
	public View getView(Context context, View convertView) {
		return null;
	}
	
	public PreferenceItem getParent() {
		return mParent;
	}
	
	public String getTitle() { return mOptionTitle; }
	public String getName() { return mOptionName; }
	
	protected void notifyChanged() {
		mOnPreferenceChangedListener.onPreferenceChanged(this);
	}
	
	public void setOnChangedListener(OnPreferenceChangedListener l) {
		mOnPreferenceChangedListener = l;
	}
	
	public void setOnDependencyStateListener(OnPreferenceDependencyStateListener l) {
		mOnDependencyStateListener = l;
	}
	
	public void store(SharedPreferences.Editor spe) {
		
	}
	
	public boolean isEnabled() {
		return mEnabled;
	}
	
	public void setEnabled(boolean state) {
		mEnabled = state;
		if (mOnDependencyStateListener!=null)
			mOnDependencyStateListener.onPreferenceStateChanged(this);
	}

	public Map<String,String> getDependencies() {
		return mDependencies;
	}

	public void setDependencies(Map<String,String> mDependencies) {
		this.mDependencies = mDependencies;
	}
}
