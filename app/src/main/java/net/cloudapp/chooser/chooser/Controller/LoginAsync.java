package net.cloudapp.chooser.chooser.Controller;

import android.content.pm.PackageInstaller;
import android.os.AsyncTask;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;

import retrofit.Callback;

/**
 * Created by t-baya on 10/7/2016.
 */

public class LoginAsync extends AsyncTask<Void, Void, Void>{
    private Callback<Void> mCallback;
    public LoginAsync(Callback<Void> callback){
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        String tokenString = token.getToken();
        String userId = token.getUserId();

        RestClient restClient = new RestClient();
        restClient.getService().login(tokenString, userId, mCallback);

        return null;
    }
}
