package net.cloudapp.chooser.chooser.Controller.Callbacks;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginCallback implements Callback<String> {
    @Override
    public void success(String s, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
