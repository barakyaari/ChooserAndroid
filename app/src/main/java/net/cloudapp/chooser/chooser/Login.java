package net.cloudapp.chooser.chooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

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

import java.util.Arrays;


public class Login extends Activity {
    String uID;
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createLoadingDialog();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login);
        AppEventsLogger.activateApp(getApplication());
        fbLoginButton = (LoginButton) findViewById(R.id.login_fb_button);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        for (int i = 0; i < 10; i++) {
            if (accessToken != null)
                break;
            System.out.println("AccessToken not yet initialized");
            try {
                Thread.sleep(10);  //temp fix for access token
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            accessToken = AccessToken.getCurrentAccessToken();
        }

        if (accessToken == null || accessToken.isExpired()) {
            System.out.println("NULL/EXPIRED TOKEN");
            facebookLogin();
        }
        else {
            System.out.println("FOUND TOKEN");
            uID = accessToken.getUserId();
            ConnectionManager connectionManager = new ConnectionManager();
            connectionManager.setId(uID);
            AtFinishRunnable runnable = new AtFinishRunnable(connectionManager.getSessionDetails());
            loadingDialog.show();
            connectionManager.login(uID, runnable);
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
        public AtFinishRunnable(SessionDetails sessionDetails){
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

    private void serverIsDownDialog () {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Server Is Down")
                .setMessage("The server is not responding")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectionManager connectionManager = new ConnectionManager();
                        connectionManager.setId(uID);
                        AtFinishRunnable runnable = new AtFinishRunnable(connectionManager.getSessionDetails());
                        loadingDialog.show();
                        connectionManager.login(uID, runnable);
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
        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday", "user_location"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                uID = accessToken.getUserId();

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
                                    JSONObject addrObj = jObject.optJSONObject("location");
                                    if (addrObj != null) {
                                        String locationID = jObject.optJSONObject("location").getString("id");

                                        GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/" + locationID,
                                                new GraphRequest.Callback() {
                                                    @Override
                                                    public void onCompleted(GraphResponse response) {
                                                        JSONObject locJObject = response.getJSONObject();

                                                        try {
                                                            String country = locJObject.getJSONObject("location").getString("country");
                                                            ConnectionManager connectionManager = new ConnectionManager();
                                                            connectionManager.updateUser(uID, firstName, lastName, email, gender, birthDate, country);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });

                                        Bundle parameters = new Bundle();
                                        parameters.putString("fields", "location");
                                        request.setParameters(parameters);
                                        request.executeAsync();
                                    }

                                    else {
                                        ConnectionManager connectionManager = new ConnectionManager();
                                        connectionManager.updateUser(uID, firstName, lastName, email, gender, birthDate, "");
                                    }
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
                connectionManager.login(uID, runnable);
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
