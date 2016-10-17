package net.cloudapp.chooser.chooser.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.facebook.login.LoginManager;

public class ServerIsDownDialog {
    public void ShowServerIsDownDialog(final Context context){
        new AlertDialog.Builder(context)
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
                    }
                })
                .show();
    }

}
