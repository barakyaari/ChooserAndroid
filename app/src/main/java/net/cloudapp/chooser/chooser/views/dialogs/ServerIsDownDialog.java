package net.cloudapp.chooser.chooser.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;


public abstract class ServerIsDownDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Server Is Down");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("The server is not responding");
        builder.setPositiveButton("Try Again", this);
        builder.setNegativeButton("Quit", this);
        Log.d("Chooser", "Server is down dialog loaded");
        return builder.create();
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                onRetry();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;
        }
    }

    public abstract void onRetry();
}
