package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.cloudapp.chooser.chooser.Common.LoadingDialogs;
import net.cloudapp.chooser.chooser.views.LoginView;
import net.cloudapp.chooser.chooser.views.dialogs.LoadingDialog;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.support.v4.app.ActivityCompat.startActivity;

public class LoginCallback implements Callback<Void> {
    private Context mContext;
    public LoginCallback(Context context){
        mContext = context;
    }
    @Override
    public void success(Void aVoid, Response response) {
        Log.d("Chooser", "Callback running");
        Intent i = new Intent("android.intent.action.FeedView");
        Log.d("Chooser", "Starting main activity");
        LoadingDialogs.deleteLoadingDialog("login");
        mContext.startActivity(i);
    }

    @Override
    public void failure(RetrofitError error) {
        LoadingDialogs.deleteLoadingDialog("login");
    }
}
