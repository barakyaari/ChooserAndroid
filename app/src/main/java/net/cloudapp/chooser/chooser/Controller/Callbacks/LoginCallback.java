package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.content.Intent;
import android.util.Log;

import net.cloudapp.chooser.chooser.Common.LoadingDialogs;
import net.cloudapp.chooser.chooser.views.LoginView;
import net.cloudapp.chooser.chooser.views.dialogs.ServerIsDownDialog;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginCallback implements Callback<Void> {
    private LoginView loginView;
    public LoginCallback(LoginView loginView){
        this.loginView = loginView;
    }
    @Override
    public void success(Void aVoid, Response response) {
        Log.d("Chooser", "Callback running");
        Intent i = new Intent("android.intent.action.FeedView");
        Log.d("Chooser", "Starting main activity");
        LoadingDialogs.hide("login");
        loginView.startActivity(i);
    }

    @Override
    public void failure(RetrofitError error) {
        ServerIsDownDialog downDialog = new ServerIsDownDialog() {
            @Override
            public void onRetry() {
                loginView.processLoginIfTokenExists();
            }
        };


        LoadingDialogs.hide("login");
        downDialog.show(loginView.getFragmentManager(),"Server Is Down Dialog");
    }
}
