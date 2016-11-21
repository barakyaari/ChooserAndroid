package net.cloudapp.chooser.chooser.views.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ben on 22/10/2016.
 */
public class LoadingDialog extends ProgressDialog {
    private boolean isShown = false;
    public LoadingDialog (Context context, String message) {
        super(context);
        setCancelable(false);
        setInverseBackgroundForced(false);
        setMessage(message);
    }
    @Override
    public void show() {
        if (isShown)
            return;
        super.show();
        isShown = true;
    }
    
    @Override
    public void hide() {
        if (!isShown)
            return;
        super.hide();
        isShown = false;
    }
}
