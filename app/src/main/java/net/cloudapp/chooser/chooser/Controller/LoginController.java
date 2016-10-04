package net.cloudapp.chooser.chooser.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.LoginView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginController{
    public static void login(){
        SessionDetails.getInstance().setAccessToken(AccessToken.getCurrentAccessToken());
    }

}
