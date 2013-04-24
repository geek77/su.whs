package su.whs.aidl;

import android.os.Parcel;
import android.os.Parcelable;


public class NameValuePair implements org.apache.http.NameValuePair, Parcelable {
	private String mName;
	private String mValue;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public NameValuePair(Parcel src) {
		mName = src.readString();
		mValue = src.readString();
	}
	
	public NameValuePair(String name, String value) {
		mName = name;
		mValue = value;
	}

	@Override
	public void writeToParcel(Parcel dst, int flags) {
		dst.writeString(mName);
		dst.writeString(mValue);
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return mValue;
	}
	
	public static final Parcelable.Creator<NameValuePair> CREATOR = new Parcelable.Creator<NameValuePair>() {

		@Override
		public NameValuePair createFromParcel(Parcel source) {
			return new NameValuePair(source);
		}

		@Override
		public NameValuePair[] newArray(int size) {
			return new NameValuePair[size];
		}
		
	};
	
	public String toString() {
		return getName() + "=\"" + getValue() + "\"";
	}

}
