package net.cloudapp.chooser.chooser.Common;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.AccessToken;


import net.cloudapp.chooser.chooser.model.Post;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class SessionDetails extends Application {
    public String userId;
    public int numOfTokens;
    private AccessToken accessToken;

    public SessionDetails(){
        userId = "";
        numOfTokens = 0;
    }

    public final String serverAddress = "http://chooserserver.herokuapp1.com";

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
