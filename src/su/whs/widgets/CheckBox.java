package su.whs.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import su.whs.R;

public class CheckBox extends LinearLayout {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	private int _position = LEFT;
	private String _text;
	private String _summary;
	private String _summaryOn;
	private String _summaryOff;
	private boolean _checked;
	private OnCheckedChangeListener mOnCheckedChange = null;
	
	public CheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.whsCheckBox);
		try {
			_position = ta.getBoolean(R.styleable.whsCheckBox_checkBoxAtRight, false) ? RIGHT : LEFT;
			_checked = ta.getBoolean(R.styleable.whsCheckBox_checked, false);
			_text = ta.getString(R.styleable.whsCheckBox_text);
			_summary = ta.getString(R.styleable.whsCheckBox_summary);
			_summaryOn = ta.getString(R.styleable.whsCheckBox_summaryOn);
			_summaryOff = ta.getString(R.styleable.whsCheckBox_summaryOff);			
		} finally {
			ta.recycle();
		}
		
	}
	
	private android.widget.CheckBox markView;	
	private android.widget.TextView descriptionView;
	private android.widget.TextView summaryView;
	
	@Override
	protected void onFinishInflate() {
		/*
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.todo_item_view, this, true); 
		*/
		LayoutInflater li = LayoutInflater.from(getContext());
		if (_position==LEFT)
			li.inflate(R.layout.whs_checkbox_l, this);
		else 
			li.inflate(R.layout.whs_checkbox_r, this);
		
		markView = (android.widget.CheckBox)findViewById(R.id.whsCheckBoxMark);
		descriptionView = (android.widget.TextView)findViewById(R.id.whsCheckBoxText);
		summaryView = (android.widget.TextView)findViewById(R.id.whsCheckBoxSummary);
		
		if (_text!=null) {
			descriptionView.setText(_text);
		}		
		markView.setChecked(_checked);
		markView.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				onCheckedChange(arg1);				
			}});	
		summaryView.setText(selectSummaryText());
		if (_text!=null) {
			descriptionView.setText(_text);
		}
	}
	
	private void onCheckedChange(boolean state) {
		_checked = state;
		summaryView.setText(selectSummaryText());
		if (mOnCheckedChange!=null)
			mOnCheckedChange.onCheckedChanged(markView, state);
	}
	
	private String selectSummaryText() {
		String defSum = "";
		if (_summary!=null) defSum = _summary;
		if (_summaryOff==null && _summaryOn==null) {
			return defSum;
		}
		if (markView.isChecked()) {
			if (_summaryOn!=null) 
				return _summaryOn;
		} else {
			if (_summaryOff!=null)				
				return _summaryOff;
		}
		return defSum;
	}
	
	public void setTag(Object o) {
		markView.setTag(o);
	}
	
	public void setTag(int key, Object o) {
		markView.setTag(key, o);
	}
	
	public Object getTag() {
		return markView.getTag();
	}
	
	public Object getTag(int key) {
		return markView.getTag(key);
	}
	
	public void setText(CharSequence s) {		
		descriptionView.setText(s);
	}
	
	public void setText(int resid) {
		descriptionView.setText(resid);
	}
	
	public CharSequence getText() {
		return descriptionView.getText();
	}
	
	public void setSummary(CharSequence s) {
		_summary = s.toString();
		summaryView.setText(s);
	}
	
	public void setSummary(int resid) {
		_summary = getResources().getString(resid);
		summaryView.setText(resid);
	}
	
	public CharSequence getSummary() {
		return _summary;
		// return summaryView.getText();
	}
	
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
		mOnCheckedChange = l;
	}
	
	public void setChecked(boolean checked) {
		markView.setChecked(checked);
		_checked = checked;		
	}
	
	public boolean isChecked() {
		return markView.isChecked();
	}
	
	public void setEnabled(boolean s) {
		markView.setEnabled(s);
	}
}
