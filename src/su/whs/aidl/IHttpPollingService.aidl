package su.whs.aidl;
import su.whs.aidl.NameValuePair;
import su.whs.aidl.IOnHttpResponseListener;

interface IHttpPollingService {
	void get(in String url, in List<NameValuePair> headers, in List<NameValuePair> params, in IOnHttpResponseListener callback);
	void post(in String url, in List<NameValuePair> headers, in List<NameValuePair> params, in List<NameValuePair> payload, in IOnHttpResponseListener callback);
}
