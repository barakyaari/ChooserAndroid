package net.cloudapp.chooser.chooser;

import android.content.SharedPreferences;

import com.facebook.AccessToken;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Created by Barak on 10/12/2015.
 */
public class SessionDetails implements Serializable {
    public String userId;
    private AccessToken accessToken;
    public boolean usePercentage;
    public boolean skipResults;
    public String responseString;
    public GregorianCalendar currentServerTime;
    public int userTokenCount;
    public Post post;

    public SessionDetails(){
        userId = "";
    }

    public void updateSharedPrefs (SharedPreferences sharedPrefs) {
        usePercentage = sharedPrefs.getBoolean("prefUnitSwitch",usePercentage);
        skipResults = sharedPrefs.getBoolean("prefSkipResSwitch",skipResults);
    }

    public final String serverAddress = "http://192.168.14.37:3000";


    public static final SessionDetails sessionDetailsInstance = new SessionDetails();
    public static SessionDetails getInstance() {
        return sessionDetailsInstance;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return this.accessToken;
    }
}
