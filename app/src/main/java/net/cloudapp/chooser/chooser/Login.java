package net.cloudapp.chooser.chooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginManager;

import net.cloudapp.chooser.chooser.HttpConnection.Post;
import net.cloudapp.chooser.chooser.HttpConnection.RestClient;
import net.cloudapp.chooser.chooser.HttpConnection.ServerAPI;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.attr.data;


public class Login extends Activity {
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    ProgressDialog loadingDialog;
    RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeFacebookSdk();
        restClient = new RestClient();

        initializeUI();
        printAppHashId();

        RegisterFacebookLogin();
        if(AccessToken.getCurrentAccessToken() != null){
            final AccessToken token = AccessToken.getCurrentAccessToken();
            Log.d("Chooser", "Access token exists: " + token.getToken());
            Log.d("Chooser", "User ID: " + token.getUserId());
            restClient.getService().login(token.getToken(), token.getUserId(), new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    Log.d("Chooser", "Successful login on server.");
                    startMainActivity(token);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Log.d("Chooser", "Login failed on server.");
                }
            });
        }
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
       // RegisterFacebookLogin();
        Toast.makeText(this, "Token: " + AccessToken.getCurrentAccessToken().getToken(), Toast.LENGTH_LONG);
    }

    private void RegisterFacebookLogin() {
        Log.d("Chooser", "Logging in with facebook!");

        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SessionDetails sessionDetails = SessionDetails.getInstance();

                final AccessToken accessToken = loginResult.getAccessToken();
                String uID = accessToken.getUserId();
                ConnectionManager connectionManager = new ConnectionManager();
                connectionManager.setId(uID);
                loadingDialog.show();
                connectionManager.setId(accessToken.getUserId());
                sessionDetails.setAccessToken(accessToken);
                Log.d("Chooser", "Facebook call successful. Token is: " + accessToken.getToken());

                restClient.getService().login(accessToken.getToken(), accessToken.getUserId(), new Callback<Void>(){

                    @Override
                    public void success(Void aVoid, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                restClient.getService().allPosts(new Callback<List<Post>>() {
                    @Override
                    public void success(List<Post> posts, Response response) {
                        if(response.getStatus() == 200){
                            startMainActivity(accessToken);
                        }
                        else{
                            logout();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Chooser", "Login response: Server is down");
                        error.printStackTrace();
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

    private void startMainActivity(AccessToken token) {
        SessionDetails.getInstance().setAccessToken(token);
        Log.d("Chooser", "Callback running");
        loadingDialog.hide();
        Intent i = new Intent("android.intent.action.MainActivity");
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
