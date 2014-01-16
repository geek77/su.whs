package su.whs.widgets;

import java.util.ArrayList;
import java.util.List;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckboxStatesController {
	private Object mLock = new Object();
	private List<CheckBox> mCheckboxes = new ArrayList<CheckBox>();
	private OnCheckedChangeListener mListener = new OnCheckedChangeListener() {		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			
		}
	};
	public CheckboxStatesController() {
		
	}
	
	public void add(CheckBox cb) {
		synchronized(mLock) {
			mCheckboxes.add(cb);
		}
	}
}
