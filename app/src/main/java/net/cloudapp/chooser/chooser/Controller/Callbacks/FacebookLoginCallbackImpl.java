package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.LoginController;

/**
 * Created by t-baya on 10/7/2016.
 */

public class FacebookLoginCallbackImpl implements FacebookCallback<LoginResult> {

    @Override
    public void onSuccess(LoginResult loginResult) {
        SessionDetails sessionDetails = SessionDetails.getInstance();
        final AccessToken accessToken = loginResult.getAccessToken();
        sessionDetails.setAccessToken(accessToken);
        LoginController.login();
    }

    @Override
    public void onCancel() {
        Log.d("Chooser", "Facebook login cancelled");

    }

    @Override
    public void onError(FacebookException error) {
        Log.e("Chooser", "Facebook login Error");
        error.printStackTrace();
    }
}
