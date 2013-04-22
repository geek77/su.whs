package su.whs.widgets;

import su.whs.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PincodeBox extends android.support.v4.app.Fragment {
	private View view;
	private TextView passwordView;
	private String expectedPassword = null;
	private StringBuilder sb = new StringBuilder();
	private OnPincodeInputListener mOnInput = null;
	
	public interface OnPincodeInputListener {
		public void onCorrectPincode();
		public void onIncorrectPincode();
	}
	
	@Override
	public void onAttach(Activity activity) {
		if (activity instanceof OnPincodeInputListener) {
			mOnInput = (OnPincodeInputListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implemenet PincodeBox.OnPincodeInputListener");
		}
		Intent intent = activity.getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				expectedPassword = extras.getString("password_hash");
				Log.d("PincodeBox", "Fragment receive expectedPassword = " + expectedPassword);
			}
		}
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pinbox_widget, container, false);
		initializeInput();
		return view;
	}
	
	public void setOnPincodeInputListener(OnPincodeInputListener listener) {
		mOnInput = listener;
	}
	
	public void setExpectedPincode(String pcode) {
		expectedPassword = pcode;
	}
	
	public View findViewById(int id) {
		return view.findViewById(id);
	}
	
	private void initializeInput() {
		Button delete = (Button)findViewById(R.id.backspaceBtn);
		delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) { backspace(); }});		
		// 0
		Button _b = (Button)findViewById(R.id.button0);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) { enterDigit("0"); }});
		// 1
		_b = (Button)findViewById(R.id.button1);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) { enterDigit("1");	}});
		// 2
		_b = (Button)findViewById(R.id.button2);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("2"); }});
		// 3
		_b = (Button)findViewById(R.id.button3);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("3"); }});
		// 4
		_b = (Button)findViewById(R.id.button4);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("4"); }});
		// 5
		_b = (Button)findViewById(R.id.button5);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("5"); }});
		// 6 
		_b = (Button)findViewById(R.id.button6);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("6"); }});
		// 7
		_b = (Button)findViewById(R.id.button7);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("7"); }});
		// 8
		_b = (Button)findViewById(R.id.button8);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("8"); }});
		// 9
		_b = (Button)findViewById(R.id.button9);
		_b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	enterDigit("9"); }});
		
		passwordView = (EditText)findViewById(R.id.pinBox);
		passwordView.setInputType(InputType.TYPE_NULL); // disable soft input
		passwordView.addTextChangedListener(new TextWatcher() {

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
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	private void enterDigit(String c) {
		sb.append(c);
		Log.i("INPUT", "entered digit '" + c + "' total sb='"+sb.toString()+"'");
		passwordView.append(c);		
		if (mOnInput != null && sb.toString().equals(expectedPassword) && expectedPassword != null && !expectedPassword.equals("")) { // entered password matches expecting
			mOnInput.onCorrectPincode();			
		} else if (mOnInput != null) {
			mOnInput.onIncorrectPincode();
		}
	}

	private void backspace() {
		if (sb.length()>0) {
			sb.deleteCharAt(sb.length()-1);
			passwordView.setText(sb.toString());
		}	
	}

	// public dispatchKeyEvent
	public void dispatchKeyEvent(KeyEvent event) {
		if (event.getAction()!=KeyEvent.ACTION_UP)
			return;
		switch(event.getKeyCode()) {
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9:
				char c = event.getMatch(new char[] { '0','1','2','3','4','5','6','7','8','9' });
				if (c>0) {
					enterDigit("" + c);
				}
			break;
			case KeyEvent.KEYCODE_DEL:
				backspace();
				break;
		}

	}
	
	// public clear
	public void clear() {
		this.passwordView.setText("");
		if (this.sb==null)
			this.sb = new StringBuilder();
		if (sb.length()>0)
			sb.delete(0, sb.length() - 1);
	}
	
}
