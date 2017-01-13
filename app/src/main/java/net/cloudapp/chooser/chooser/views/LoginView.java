package net.cloudapp.chooser.chooser.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.cloudapp.chooser.chooser.Common.LoadingDialogs;
import net.cloudapp.chooser.chooser.Controller.Callbacks.FacebookLoginCallbackImpl;
import net.cloudapp.chooser.chooser.Controller.Callbacks.LoginCallback;
import net.cloudapp.chooser.chooser.Controller.LoginController;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.views.dialogs.LoadingDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginView extends Activity {
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    public boolean processLoginUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
        RegisterFacebookLogin();
        LoadingDialogs.addLoadingDialog(this, "login", "Logging in...\nPlease Wait");
        Log.d("Chooser", "Login loaded");
    }

    public synchronized void processLoginIfTokenExists() {
        if (AccessToken.getCurrentAccessToken() != null && !processLoginUsed){
            processLoginUsed = true;
            LoadingDialogs.show("login");
            LoginCallback callback = new LoginCallback(this);
            LoginController loginController = new LoginController(callback);
            loginController.login();
        }
    }

    public void initializeComponents(){
        initializeFacebookSdk();
        initializeUI();
        printAppHashId();
        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("Chooser", "Loading from AccessToken Changed");
                processLoginIfTokenExists();
            }
        };
    }

    private void initializeFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(getApplication());
    }

    private void initializeUI() {
        setContentView(R.layout.login);
        fbLoginButton = (LoginButton) findViewById(R.id.login_fb_button);
    }

    private void printAppHashId() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "net.cloudapp.chooser.chooser",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        processLoginIfTokenExists();
    }

    private void RegisterFacebookLogin() {
        Log.d("Chooser", "Logging in with facebook!");

        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));
        fbLoginButton.registerCallback(callbackManager, new FacebookLoginCallbackImpl());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }
}
