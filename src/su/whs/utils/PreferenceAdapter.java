package su.whs.utils;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class PreferenceAdapter extends ArrayAdapter<PreferenceItem> {

	public PreferenceAdapter(Context context, int resource,
			int textViewResourceId, List<PreferenceItem> objects) {
		super(context, resource, textViewResourceId, objects);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItem(position).getView(getContext(),convertView);
	}

}
