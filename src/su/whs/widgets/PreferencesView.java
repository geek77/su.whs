package su.whs.widgets;

import java.util.List;

import su.whs.R;
import su.whs.R.styleable;
import su.whs.utils.PreferenceItem;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * TODO: document your custom view class.
 */
public class PreferencesView extends LinearLayout {
	private ListView mListView = null;
	private List<PreferenceItem> mItems = null;
	public interface OnHeaderSelectedListener {
		void onHeaderSelected(String tag);
	}
	
	public PreferencesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}
	
	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray ta = getContext().obtainStyledAttributes(attrs,
				R.styleable.PreferencesView, defStyle, 0);

		ta.recycle();
	}

	@Override 
	protected void onFinishInflate() {
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.prefs_list_view, this);
		mListView = (ListView) findViewById(R.id.prefListView);		
	}
	
}
