package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;

import retrofit.Callback;

public class LoginController{
    private static Callback<Void> mCallback;

    public LoginController(Callback<Void> loginCallback){
        mCallback = loginCallback;
    }

    public static void login(){
        SessionDetails.getInstance().setAccessToken(AccessToken.getCurrentAccessToken());
        LoginAsyncTask loginAsync = new LoginAsyncTask(mCallback);
        loginAsync.execute();
    }

}
