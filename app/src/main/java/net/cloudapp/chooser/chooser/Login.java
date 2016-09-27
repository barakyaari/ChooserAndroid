package net.cloudapp.chooser.chooser;

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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import net.cloudapp.chooser.chooser.HttpConnection.ServerAPI;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.data;


public class Login extends Activity {
    AccessToken accessToken;
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        ConnectionManager connectionManager = new ConnectionManager();
                        connectionManager.setId(accessToken.getUserId());
                        loadingDialog.show();
                        SessionDetails.getInstance().setAccessToken(accessToken);
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
        RegisterFacebookLogin();
    }

    private void RegisterFacebookLogin() {
        Log.d("Chooser", "Logging in with facebook!");

        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Chooser", "Facebook call successful");

                final AccessToken accessToken = loginResult.getAccessToken();
                String uID = accessToken.getUserId();
                ConnectionManager connectionManager = new ConnectionManager();
                connectionManager.setId(uID);
                loadingDialog.show();
                connectionManager.setId(accessToken.getUserId());
                SessionDetails.getInstance().setAccessToken(accessToken);
                String chooserServerAddress = "http://192.168.14.37:3000";

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(chooserServerAddress)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                ServerAPI serverAPI = retrofit.create(ServerAPI.class);

                Call call = serverAPI.login(accessToken.getToken(), accessToken.getUserId());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.code() == 200){
                            SessionDetails.getInstance().setAccessToken(accessToken);
                            Log.d("Chooser", "Callback running");
                            loadingDialog.hide();
                            Intent i = new Intent("android.intent.action.MainActivity");
                            Log.d("Chooser", "Starting main activity");
                            startActivity(i);
                        }
                        else{
                            logout();
                        }
                        }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("Chooser", "Login response: Server is down");
                        serverIsDownDialog();
                    }
                });
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

        });

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
