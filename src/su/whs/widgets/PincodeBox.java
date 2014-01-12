package su.whs.widgets;

import su.whs.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PincodeBox extends android.support.v4.app.Fragment {
	private View view;
	private TextView passwordView;
	private String expectedPassword = null;
	private TextView labelView;
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
		labelView = (TextView) view.findViewById(R.id.lockLabel);
		initializeInput();
		return view;
	}
	
	public void setLabel(String text, Drawable icon) {
		labelView.setText(text);
		labelView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
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
		passwordView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int inType = passwordView.getInputType(); // backup the input type
			    passwordView.setInputType(InputType.TYPE_NULL); // disable soft input
			    passwordView.onTouchEvent(event); // call native handler
			    passwordView.setInputType(inType); // restore input type
			    return true; // consume touch even
			}
		});
		/* TODO: check for deprecation
		passwordView.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				// stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// stub
			}

			@Override
			public void onTextChanged(CharSequence seq, int arg1, int arg2,
					int arg3) {
				if (seq.toString().equals(expectedPassword) && expectedPassword !=null && !expectedPassword.equals("")) {
					if (mOnInput!=null) 
						mOnInput.onCorrectPincode();
				}
			}
		}); */
	}

	private void enterDigit(String c) {
		Log.i("INPUT", "entered digit '" + c + "' total sb='"+passwordView.getText().toString()+"'");
		passwordView.append(c);		
		if (mOnInput != null && passwordView.getText().toString().equals(expectedPassword) && expectedPassword != null && !expectedPassword.equals("")) { // entered password matches expecting
			mOnInput.onCorrectPincode();			
		} else if (mOnInput != null) {
			mOnInput.onIncorrectPincode();
		}
	}

	private void backspace() {
		String text = passwordView.getText().toString();
		if (text.length()>0) {
			passwordView.setText(text.substring(0, text.length()-1));
		} else {
			passwordView.setText("");
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
	}
	
}
