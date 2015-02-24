package firebase.saveandroidcontacttofirebase.ui;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by gokhan on 2/24/15.
 */
public class RegisterFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
    }
}
