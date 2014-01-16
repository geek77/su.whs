package su.whs.widgets;

import java.util.ArrayList;
import java.util.List;

import su.whs.R;
import su.whs.utils.PreferenceAdapter;
import su.whs.utils.PreferenceItem;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * TODO: document your custom view class.
 */
public class PreferencesView extends LinearLayout {
	private ListView mListView = null;
	private PreferenceAdapter mAdapter = null;
	public interface OnHeaderSelectedListener {
		void onHeaderSelected(String tag);
	}
	
	public PreferencesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
		mAdapter = new PreferenceAdapter(context, 0, 0, new ArrayList<PreferenceItem>());
	}
	
	public PreferencesView(Context context, AttributeSet attrs, List<PreferenceItem> items) {
		super(context,attrs);
		mAdapter = new PreferenceAdapter(context, 0, 0, items);
	}
	
	public void add(List<PreferenceItem> items) {
		for (PreferenceItem i: items) {
			mAdapter.add(i);
		}
	}
	
	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray ta = getContext().obtainStyledAttributes(attrs,
				R.styleable.whsPreferencesView, defStyle, 0);

		ta.recycle();
	}

	@Override 
	protected void onFinishInflate() {
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.prefs_list_view, this);
		mListView = (ListView) findViewById(R.id.prefListView);
		mListView.setAdapter(mAdapter);
	}
	
	public ArrayAdapter<PreferenceItem> getAdapter() {
		return mAdapter;
	}
	
	
	
}
