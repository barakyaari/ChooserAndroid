package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;

import net.cloudapp.chooser.chooser.Common.SessionDetails;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



public abstract class PromoteCallback implements Callback<JsonObject> {
    private Context context;
    public static final int PROMOTION_COST = 50;


    public PromoteCallback(Context context){
        this.context = context;
    }

    @Override
    public void success(JsonObject result, Response response) {
        SessionDetails.getInstance().numOfTokens -= PROMOTION_COST;
        Toast.makeText(context, "Post successfully promoted!", Toast.LENGTH_LONG).show();
        callInSuccess();
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }

    public abstract void callInSuccess();
}
