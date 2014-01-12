package su.whs.utils;

import su.whs.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class PreferenceCheckBox extends PreferenceSwitcher {
	class Holder {
		public TextView mTitle;
		public TextView mSummary;
		public CheckBox mCheck;
	}
	
	// private OnCheckedChangeListener mOnCheckedChangeListener = 
		
	public PreferenceCheckBox(AttributeSet attr, PreferenceItem parent) {
		super(attr,parent);
	}
		
	
	public View getView(Context context, View convertView) {
		View row = convertView;
		final Holder h; 
		if (row==null || row.getTag() == null || !(row.getTag() instanceof Holder)) {
			h = new Holder();
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = li.inflate(R.layout.whs_checkbox_r, null);
			h.mTitle = (TextView) row.findViewById(R.id.whsCheckBoxText);
			h.mSummary = (TextView) row.findViewById(R.id.whsCheckBoxSummary);			
			h.mCheck = (CheckBox) row.findViewById(R.id.whsCheckBoxMark);
			h.mCheck.setChecked(mChecked);
			row.setTag(h);
			h.mCheck.setTag(this);
			h.mCheck.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {					
					PreferenceCheckBox pi = (PreferenceCheckBox) arg0.getTag();
					// if (!pi.mHandleOnChecked) return;					
					pi.mChecked = arg1;
					h.mSummary.setText(getSummary());
					pi.notifyChanged();
				}}); 
		} else {
			h = (Holder) row.getTag();
			// mHandleOnChecked = false;
			// h.mCheck.setChecked(mChecked);
			// mHandleOnChecked = true;
		}
		h.mSummary.setTag(this);
		fillHolder(h);
		return row;
	}
	
	private void fillHolder(Holder h) {
		String summary = getSummary();
		String title = getTitle();
		h.mSummary.setText(summary);
		h.mTitle.setText(title);
	}
}
