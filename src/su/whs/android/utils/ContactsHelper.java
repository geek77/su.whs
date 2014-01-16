package su.whs.android.utils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

public class ContactsHelper {
	
	public class Contact {
		private int mId;
		private String mDisplayName;
		private String mPhoneNumber;
		private String mPhotoUri;
		private String mEmail;
		
		public Contact(int id, String dn, String pn, String photoUri) {
			mId = id;
			mDisplayName = dn;
			mPhoneNumber = pn;
			mPhotoUri = photoUri;
		}
		public int getId() { return mId; }
		public String getDisplayName() { return mDisplayName; }
		public String getPhoneNumber() { return mPhoneNumber; }
		public String getPhotoURI() { return mPhotoUri; }
		public String getEmail() { return mEmail; }
	}

	private List<Contact> mContacts = new ArrayList<ContactsHelper.Contact>();
	
	@SuppressLint("InlinedApi")
	public ContactsHelper(Context context) {
		Cursor cursor = context.getContentResolver()
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
		if (cursor.moveToFirst()) {
			int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
			int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			int idIdx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int photoId;
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			        photoId = cursor.getColumnIndex(Contacts.PHOTO_URI);
			    } else {
			        photoId = idIdx;
			    }
			while(cursor.moveToNext()) {
				String name = cursor.getString(nameIdx);
				String phone = cursor.getString(phoneIdx);
				int id = cursor.getInt(idIdx);
				String photo = cursor.getString(photoId);
				mContacts.add(new Contact(id,name,phone,contactPhotoURI(photo)));
			}
		}
	}
	
	public Contact getContact(int pos) { return mContacts.get(pos); }
	public int getCount() { return mContacts.size(); }
	
	public String contactPhotoURI(String photo) {
		if (Build.VERSION.SDK_INT
                >=
            Build.VERSION_CODES.HONEYCOMB) {
            return photo;
        } else {
        	Uri contactUri = Uri.withAppendedPath(
                    Contacts.CONTENT_URI, photo);
        	return Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY).toString();
        }
	}
	
	public static Bitmap getContactPhotoBitmap(Context context, String uri) {
		AssetFileDescriptor afd = null;
		try {
			afd = context.getContentResolver().
                openAssetFileDescriptor(Uri.parse(uri), "r");
			FileDescriptor fileDescriptor = afd.getFileDescriptor();
		
			if (fileDescriptor != null) {
				return BitmapFactory.decodeFileDescriptor(
                    fileDescriptor, null, null);
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {}
            }
        }
        return null;
	}
}
