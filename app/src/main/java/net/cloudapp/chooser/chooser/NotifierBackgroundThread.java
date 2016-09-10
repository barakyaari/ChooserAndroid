package net.cloudapp.chooser.chooser;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Ben on 29/08/2016.
 */


public class NotifierBackgroundThread extends IntentService {

    public NotifierBackgroundThread() {
        super("Notifier Background Thread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Check vote count
    }
}

