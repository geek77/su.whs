package su.whs.widgets;

import java.util.List;

import su.whs.widgets.InstalledApplicationAdapter;
import su.whs.utils.InstalledApplicationModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import su.whs.R;

public class AppListFragment extends Fragment {
	private View view = null;
	private String title = "";
	
	public interface OnSelectionChangedListener {
		boolean OnApplicationSelectionChanged(int position, boolean checked);
	}
	
	public interface ApplicationsListProvider {
		List<InstalledApplicationModel> all();
	}
	
	@SuppressWarnings("unused")
	private OnSelectionChangedListener mOnSelection = null;
	private ApplicationsListProvider mProv = null;	
	
	@Override
	public void onAttach(Activity activity) {		
		if (activity instanceof OnSelectionChangedListener && activity instanceof ApplicationsListProvider) {
			mOnSelection = (OnSelectionChangedListener) activity;
			mProv = (ApplicationsListProvider) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implemenet AppListFragment[ .ApplicationsListProvider, .OnSelectionChangedListener ]");
		}
		Intent intent = activity.getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				title = extras.getString("app_list_title");
				if (title==null) title = "untitled";
			}
		}	
		super.onAttach(activity);
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_list_fragment, container, false);
		TextView titleView = (TextView) view.findViewById(R.id.appListTitle);
		titleView.setText(title);
		ListView lv = (ListView) view.findViewById(R.id.appListView);
		lv.setItemsCanFocus(false);
		InstalledApplicationAdapter adapt = new InstalledApplicationAdapter(getActivity(),mProv.all(),0);		
		lv.setAdapter(adapt);		
		return view;
	}
	
	public void NotifyUpdate(ApplicationsListProvider prov) {
		mProv = prov;
		InstalledApplicationAdapter adapt = new InstalledApplicationAdapter(getActivity(),mProv.all(),0);
		ListView lv = (ListView) view.findViewById(R.id.appListView);
		lv.setAdapter(adapt);
	}
}
