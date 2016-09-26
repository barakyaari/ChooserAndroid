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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static android.R.attr.fragment;


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

        facebookLogin();


        //accessToken = AccessToken.getCurrentAccessToken();
//        for (int i = 0; i < 10; i++) {
//            if (accessToken != null)
//                break;
//            System.out.println("AccessToken not yet initialized");
//            try {
//                Thread.sleep(10);  //temp fix for access token
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            accessToken = AccessToken.getCurrentAccessToken();
//        }

        if (accessToken == null || accessToken.isExpired()) {
            System.out.println("NULL/EXPIRED TOKEN");
            facebookLogin();
        } else {
            System.out.println("FOUND TOKEN");
            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("email")
            );
            ConnectionManager connectionManager = new ConnectionManager();
            connectionManager.setId(accessToken.getUserId());
            AtFinishRunnable runnable = new AtFinishRunnable(connectionManager.getSessionDetails());
            loadingDialog.show();
            connectionManager.login(accessToken, runnable);
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

    public class AtFinishRunnable implements Runnable {
        private SessionDetails sessionDetails;

        public AtFinishRunnable(SessionDetails sessionDetails) {
            this.sessionDetails = sessionDetails;
        }

        @Override
        public void run() {
            loadingDialog.hide();
            if (sessionDetails.responseString.equals("-2")) {
                serverIsDownDialog();
            } else {
                sessionDetails.userTokenCount = Integer.valueOf(sessionDetails.responseString);
                Intent i = new Intent("android.intent.action.MainActivity");
                i.putExtra("SessionDetails", sessionDetails);
                startActivity(i);
            }
        }
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
                        AtFinishRunnable runnable = new AtFinishRunnable(connectionManager.getSessionDetails());
                        loadingDialog.show();
                        connectionManager.login(accessToken, runnable);
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
        facebookLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void facebookLogin() {
        Log.d("Chooser", "Logging in with facebook!");
        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                String uID = accessToken.getUserId();

                GraphRequest request = GraphRequest.newMeRequest(accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jObject, GraphResponse response) {
                                try {
                                    final String uID = jObject.getString("id");
                                    final String firstName = jObject.getString("first_name");
                                    final String lastName = jObject.getString("last_name");
                                    final String email = jObject.getString("email");
                                    final String gender = jObject.getString("gender");
                                    final String birthDate = jObject.getString("birthday");

                                    GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/",
                                            new GraphRequest.Callback() {
                                                @Override
                                                public void onCompleted(GraphResponse response) {
                                                    JSONObject locJObject = response.getJSONObject();
                                                    ConnectionManager connectionManager = new ConnectionManager();
                                                    connectionManager.updateUser(uID, firstName, lastName, email, gender, birthDate);
                                                }
                                            });

                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "location");
                                    request.setParameters(parameters);
                                    request.executeAsync();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();

                ConnectionManager connectionManager = new ConnectionManager();
                connectionManager.setId(uID);
                AtFinishRunnable runnable = new AtFinishRunnable(connectionManager.getSessionDetails());
                loadingDialog.show();
                connectionManager.login(accessToken, runnable);
            }

            @Override
            public void onCancel() {
                System.out.println("CANCELLED");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("ERROR");
            }
        });
    }


}
