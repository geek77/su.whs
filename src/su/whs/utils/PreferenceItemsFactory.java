package su.whs.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Xml;

public class PreferenceItemsFactory {
	private Map<String,PreferenceItem> mItems = new HashMap<String,PreferenceItem>();
	
	public PreferenceItemsFactory(XmlPullParser xml) throws XmlPullParserException, IOException {
		init(xml);
	}
	
	public PreferenceItemsFactory(Context context, String prefsFileName) throws XmlPullParserException, IOException {
		init(context,prefsFileName);
	}
	
	public PreferenceItemsFactory(Context context) throws XmlPullParserException, IOException {
		init(context,context.getPackageName()+".prefs.xml");
	}
	
	private void init(Context context, String prefsFileName) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xml = factory.newPullParser();				
		xml.setInput(context.openFileInput(prefsFileName), "utf-8");
		init(xml);
	}
	
	private void init(XmlPullParser xml) throws XmlPullParserException, IOException {
		int eventType = xml.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT) {
			switch(eventType) {
			case XmlPullParser.START_TAG:
				readPreferenceItem(xml,eventType);
				break;
				default:
					break;
			}
		}
	}
	
	private PreferenceItem readPreferenceItem(XmlPullParser xml, int eventType) throws XmlPullParserException, IOException {
		String name = xml.getName();
		AttributeSet attributes = Xml.asAttributeSet(xml);
		if (name.equals("CheckBoxPreference")) {
			readToEndTag(xml,eventType);
			return makeCheckBoxPreference(attributes);
		} else if (name.equals("EditTextPreference")) {
			readToEndTag(xml,eventType);
			return makeEditTextPreference(attributes);
		} else if (name.equals("ListPreference")) {
			readToEndTag(xml,eventType);
			return makeListPreference(attributes);
		} else if (name.equals("PreferenceCategory")) {
			
		} else if (name.equals("PreferenceScreen")) {
			
		}
		return null;
	}
	
	private void readToEndTag(XmlPullParser xml, int eventType) throws XmlPullParserException, IOException {
		while (eventType!=XmlPullParser.END_TAG)
			eventType = xml.next();
	}
	
	private PreferenceSwitcher makeCheckBoxPreference(AttributeSet as) {
		return null;
	}
	
	private PreferenceEditText makeEditTextPreference(AttributeSet as) {
		return null;
	}
	
	private PreferenceList makeListPreference(AttributeSet as) {
		return null;
	}
	
	public PreferenceItem getItem(String pref) {
		return null;
	}
	
	public void enable(String pref) {
		
	}
	
	public void disable(String pref) {
		
	}

	public Map<String,PreferenceItem> getItems() {
		return mItems;
	}

	public void setItems(Map<String,PreferenceItem> mItems) {
		this.mItems = mItems;
	}

}
