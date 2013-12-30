package su.whs.aidl;

interface IOnHttpResponseListener {
	void onHttpResponse(int resultCode, String resultBody);
	void onRepeat();
}