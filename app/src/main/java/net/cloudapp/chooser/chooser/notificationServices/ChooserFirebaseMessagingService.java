package net.cloudapp.chooser.chooser.notificationServices;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ben on 14/11/2016.
 */

public class ChooserFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Notifier.sendNotification(message.getNotification().getBody(), this);
    }



}
