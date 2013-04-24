package su.whs.utils;

import su.whs.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PreferenceEditText extends PreferenceItem {
	private boolean mIsPassword = false;
	@SuppressWarnings("unused")
	private boolean mIsNumeric = false;
	private String mValue;
	@SuppressWarnings("unused")
	private String mHint;
	private Context mContext;
	private String mPasswordMismatch = "passwords does not matches";
	public PreferenceEditText(AttributeSet attr, PreferenceItem parent) {
		super(attr, parent);
		init(attr);
	}
	
	private void init(AttributeSet attr) {
		String inputType = attr.getAttributeValue("android", "inputType");
		if (inputType!=null) {
			if (inputType.startsWith("number")){
				mIsNumeric = true;				
			}
			if (inputType.endsWith("Password")) mIsPassword = true;
		}
		mValue = attr.getAttributeValue("android", "value");
		mHint = attr.getAttributeValue("android", "hint");
		
	}
	
	class Holder {
		public TextView mText;
		public TextView mValue;
		public PreferenceEditText mPref;
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			PreferenceEditText pet = (PreferenceEditText) arg0.findViewById(R.id.prefsEditTextText).getTag();
			pet.showEditDialog();
		}	
		
	};
	
	public View getView(Context context, View convertView) {
		View row = convertView;
		Holder h;
		if (row==null||!(row.getTag() instanceof Holder)) {
			h = new Holder();
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = li.inflate(R.layout.prefs_edit_text, null);
			h.mText = (TextView) row.findViewById(R.id.prefsEditTextText);
			h.mValue = (TextView) row.findViewById(R.id.prefsEditTextValue);
			row.setOnClickListener(mOnClickListener);
			row.setTag(h);
		} else {
			h = (Holder) row.getTag();
		}
		h.mText.setTag(this);
		h.mText.setText(getTitle());
		h.mValue.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		h.mValue.setTag(getValue());
		mContext = context;
		return row;
	}
	
	private void showPasswordEditDialog() {
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View passcodeDialog = li.inflate(R.layout.prefs_password_edit_dialog, null);
		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setView(passcodeDialog);
		
		final EditText pc1 = (EditText) passcodeDialog.findViewById(R.id.prefsPasswordEditText1);
		final EditText pc2 = (EditText) passcodeDialog.findViewById(R.id.prefsPasswordEditText2);
		final TextView message = (TextView) passcodeDialog.findViewById(R.id.prefsPasswordEditMessage);
		
		Button cancel = (Button)passcodeDialog.findViewById(R.id.cancelPasswordChangeBtn);
		final Button ok = (Button)passcodeDialog.findViewById(R.id.savePasswordBtn);
		ok.setEnabled(false);
		
		final AlertDialog dia = dialog.create();
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pc1.getEditableText().clear();
				pc2.getEditableText().clear();
				dia.dismiss();
			}});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// FIXME: add call update here
				PreferenceEditText.this.mValue = pc2.getText().toString();				
				dia.dismiss();
				PreferenceEditText.this.notifyChanged();
				
			}});
		pc2.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (pc1.getText().toString().equals(pc2.getText().toString())) {
					// passcode matches - enable Ok button
					
					message.setVisibility(View.GONE);
					ok.setEnabled(true);
				} else {
					ok.setEnabled(false);
					message.setTextColor(Color.RED);
					message.setText(mPasswordMismatch);
					message.setVisibility(View.VISIBLE);
				}
				
			}});
		dia.show();
	}
	
	private void showInputDialog() {
		// FIXME: implement
	}
	
	public void showEditDialog() {
		if (mIsPassword) 
			showPasswordEditDialog();
		else {
			showInputDialog();
		}
	}

	public String getValue() {
		return mValue;
	}
	
	public void store(SharedPreferences.Editor spe) {
		spe.putString(getName(), getValue());
	}
}
