package net.cloudapp.chooser.chooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Barak on 08/12/2015.
 */
public class Login extends Activity implements View.OnClickListener{
    TextView idTextView;
    EditText usernameEditText;
    EditText passwordEditText;
    String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        idTextView = (TextView) findViewById(R.id.idTextView);
        Button buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(this);
    }

    public class AtFinishRunnable implements Runnable{
        private SessionDetails sessionDetails;
        public AtFinishRunnable(SessionDetails sessionDetails){
            this.sessionDetails = sessionDetails;
        }

        @Override
        public void run(){
            String responseText = sessionDetails.responseString;
            sessionDetails.userId = Integer.parseInt(responseText);
            idTextView.setText(responseText);
            if(sessionDetails.userId > 0){
                Intent i = new Intent("android.intent.action.MainActivity");
                i.putExtra("SessionDetails", sessionDetails);
                startActivity(i);
            }
        }

    }

    private void login(){
        ConnectionManager connectionManager = new ConnectionManager();
        AtFinishRunnable runnable = new AtFinishRunnable(connectionManager.getSessionDetails());
        String username = usernameEditText.getText().toString().toLowerCase().trim();
        String password = passwordEditText.getText().toString();
        connectionManager.Login(username, password, runnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                login();
                break;
        }
    }
}
