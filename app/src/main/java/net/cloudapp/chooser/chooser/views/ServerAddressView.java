package net.cloudapp.chooser.chooser.views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.R;

import java.util.List;

public class ServerAddressView extends Activity {
    Button connectToServerButton;
    ListView serverAddressListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_server_address);

        serverAddressListView = (ListView) findViewById(R.id.serverAddressListView);
        String[] values = new String[] {
                "http://chooserserver.herokuapp.com",
                "http://192.168.14.104:3000",
                "http://10.0.0.1:3000",
                "http://192.168.43.2:3000"
        };

        final ArrayAdapter<String> addresses = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                values);
        serverAddressListView.setAdapter(addresses);

        connectToServerButton = (Button) findViewById(R.id.connectToServerButton);

        serverAddressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionDetails.getInstance().serverAddress = addresses.getItem(position).toString();
                Intent i = new Intent("android.intent.action.LoginView");
                Log.d("Chooser", "Starting login activity");
                startActivity(i);
            }
        });
    }
}
