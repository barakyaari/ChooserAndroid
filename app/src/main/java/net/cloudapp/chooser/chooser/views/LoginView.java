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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.cloudapp.chooser.chooser.Controller.Callbacks.FacebookLoginCallbackImpl;
import net.cloudapp.chooser.chooser.Controller.Callbacks.LoginCallback;
import net.cloudapp.chooser.chooser.Controller.LoginController;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginView extends Activity{
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    ProgressDialog loadingDialog;
    RestClient restClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
        processLoginIfTokenExists();

        Log.d("Chooser", "Login loaded");

    }

    private void processLoginIfTokenExists() {
        if(AccessToken.getCurrentAccessToken() != null){
            LoginCallback callback = new LoginCallback(this);
            LoginController loginController = new LoginController(callback);
            loginController.login();
            //loadFeed();
        }
    }

    public void endActivity(){
        loadingDialog.hide();
        finish();

    }

    public void initializeComponents(){
        initializeFacebookSdk();
        initializeUI();
        printAppHashId();
        RegisterFacebookLogin();
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
        createLoadingDialog();
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

    public void createLoadingDialog() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Connecting to Server...\nPlease Wait");
        loadingDialog.setCancelable(false);
        loadingDialog.setInverseBackgroundForced(false);
    }

    private void serverIsDownDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Server Is Down")
                .setMessage("The server is not responding")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Chooser", "Not implemented!");
                    }

                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                        finish();
                    }

                })
                .show();
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

    private void loadFeed() {
        Log.d("Chooser", "Callback running");
        loadingDialog.hide();
        Intent i = new Intent("android.intent.action.Feed");
        Log.d("Chooser", "Starting main activity");
        startActivity(i);
    }

    private void logout() {
        //TODO: implement
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }
}
