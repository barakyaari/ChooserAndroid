package net.cloudapp.chooser.chooser.views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.R;

public class ServerAddressView extends Activity {
    Button connectToServerButton;
    EditText serverAddressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_server_address);

        connectToServerButton = (Button) findViewById(R.id.connectToServerButton);
        serverAddressEditText = (EditText) findViewById(R.id.serverAddressEditText);

        serverAddressEditText.setText(SessionDetails.getInstance().serverAddress);

        connectToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverAddress = serverAddressEditText.getText().toString();
                SessionDetails sessionDetails = SessionDetails.getInstance();
                sessionDetails.serverAddress = serverAddress;

                Intent i = new Intent("android.intent.action.LoginView");
                Log.d("Chooser", "Starting login activity");
                startActivity(i);
            }
        });
    }
}
