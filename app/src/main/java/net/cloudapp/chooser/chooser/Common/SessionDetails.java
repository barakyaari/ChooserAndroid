package net.cloudapp.chooser.chooser.Common;

import android.content.SharedPreferences;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Model.Post;

import java.io.Serializable;
import java.util.GregorianCalendar;

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

   // public final String serverAddress = "http://192.168.43.2:3000";
    //public final String serverAddress = "http://10.0.2.2:3000"; // For Virtual device
    public final String serverAddress = "http://10.100.102.12:3000"; // For Virtual device


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
