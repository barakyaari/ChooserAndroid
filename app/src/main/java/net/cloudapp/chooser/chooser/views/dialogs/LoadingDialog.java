package net.cloudapp.chooser.chooser.views.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ben on 22/10/2016.
 */
public class LoadingDialog extends ProgressDialog {
    public LoadingDialog (Context context, String message) {
        super(context);
        setCancelable(false);
        setInverseBackgroundForced(false);
        setMessage(message);
    }
}
