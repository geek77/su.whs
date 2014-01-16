package su.whs.widgets;

import java.util.ArrayList;
import java.util.List;

import android.widget.CheckBox;

public class CheckboxStatesController {
	private Object mLock = new Object();
	private List<CheckBox> mCheckboxes = new ArrayList<CheckBox>();
	public CheckboxStatesController() {
		
	}
	
	public void add(CheckBox cb) {
		synchronized(mLock) {
			mCheckboxes.add(cb);
		}
	}
}
