package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.widget.Toast;

import com.google.gson.JsonObject;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



public class PromoteCallback implements Callback<JsonObject> {
    private StatisticsView statisticsView;
    public static final int PROMOTION_COST = 50;


    public PromoteCallback(StatisticsView view){
        statisticsView = view;
    }

    @Override
    public void success(JsonObject result, Response response) {
        SessionDetails.getInstance().numOfTokens -= PROMOTION_COST;
        statisticsView.refreshPromoteButton();
        Toast.makeText(statisticsView, "Post successfully promoted!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }
}
