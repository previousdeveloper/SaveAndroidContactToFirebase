package firebase.saveandroidcontacttofirebase.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import firebase.saveandroidcontacttofirebase.R;


public class MainActivity extends ActionBarActivity {
    Firebase firebaseRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        firebaseRef  = new Firebase("https://androidspeechtotext.firebaseio.com/");
        saveToFirebase();
        queryData();
    }


    public void saveToFirebase() {

        HashMap<String, Object> users = new HashMap();
        users.put(getPhoneNumber(), getContactName());
        for (int i = 0; i < users.size(); i++) {
            firebaseRef.child(getPhoneNumber()).setValue(getContactName());
        }

    }

    public void queryData() {
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Toast.makeText(getApplicationContext(),
                        dataSnapshot.child("5077154727").getValue().toString(),
                        Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public String getContactName() {
        String name = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
            }
        }
        return name;
    }

    public String getPhoneNumber() {
        String phoneNumber = null;
        String formatedPhoneNumber = null;
        String _ID = ContactsContract.Contacts._ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver
                            .query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    }
                    phoneCursor.close();
                }
            }
        }
        formatedPhoneNumber = phoneNumber.replaceAll(" ", "").replace("+9", "").replaceFirst("0", "");

        return formatedPhoneNumber;
    }
}
