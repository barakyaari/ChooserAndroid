package net.cloudapp.chooser.chooser.notificationServices;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ben on 14/11/2016.
 */

public class ChooserFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String recentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("ChooserFirebaseService", "Token Refresh: " + recentToken);
    }
}
