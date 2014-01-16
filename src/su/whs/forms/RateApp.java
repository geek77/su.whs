package su.whs.forms;

import su.whs.widgets.ButtonR;
import su.whs.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class RateApp extends Activity {
	protected class Config {
		private SharedPreferences mPrefs;
		public Config(Context ctx) {
			mPrefs = ctx.getSharedPreferences("su.whs.rate.cfg", Context.MODE_PRIVATE);
		}
		public boolean getRated() { return mPrefs.getBoolean("rated", false); }
		public void setRated() { 
			SharedPreferences.Editor e = mPrefs.edit();
			e.putBoolean("rated", true);
			e.commit();
		}
	}

	private Config mConfig = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mConfig = new Config(getBaseContext());
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rate_app);
		String supportText = getResources().getString(R.string.rate_support_us);
		
		((TextView)findViewById(R.id.textSupportUS)).setText(Html.fromHtml(supportText));
		
		((ButtonR)findViewById(R.id.likeBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RateApp.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
				mConfig.setRated();
				finish();
			}
		});
		
		((ButtonR)findViewById(R.id.dontLikeBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setData(Uri.parse("mailto:support@figaroo.co"));
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@figaroo.co"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Отзыв");
				startActivity(i);
				mConfig.setRated();
				finish();
			}
		});
		
		((ButtonR)findViewById(R.id.rememberLaterBtn)).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
