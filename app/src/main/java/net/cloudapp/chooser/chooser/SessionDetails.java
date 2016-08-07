package net.cloudapp.chooser.chooser;

import android.content.SharedPreferences;
import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by Barak on 10/12/2015.
 */
public class SessionDetails implements Serializable {
    public String userId;
    public String responseString;
    public boolean usePercentage;
    public boolean skipResults;
    public int userTokenCount;
    public PostSerializable post;
    public GregorianCalendar currentServerTime;


    public SessionDetails(){
        userId = "";
        userTokenCount = 0;
    }

    public void updateSharedPrefs (SharedPreferences sharedPrefs) {
        usePercentage = sharedPrefs.getBoolean("prefUnitSwitch",usePercentage);
        skipResults = sharedPrefs.getBoolean("prefSkipResSwitch",skipResults);
    }
}
