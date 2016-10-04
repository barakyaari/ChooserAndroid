package net.cloudapp.chooser.chooser.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Views.LoginView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginController extends Activity{
    Login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginView.initializeView();


        if(AccessToken.getCurrentAccessToken() != null){
            final AccessToken token = AccessToken.getCurrentAccessToken();
            Log.d("Chooser", "Access token exists: " + token.getToken());
            Log.d("Chooser", "User ID: " + token.getUserId());
            restClient.getService().login(token.getToken(), token.getUserId(), new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    Log.d("Chooser", "Successful login on server.");
                    loadFeed();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Log.d("Chooser", "LoginView failed on server.");
                }
            });
        }
    }
}
