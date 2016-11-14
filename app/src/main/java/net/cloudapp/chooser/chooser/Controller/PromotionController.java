package net.cloudapp.chooser.chooser.Controller;

import android.util.Log;
import android.widget.Button;

import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.FeedView;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user1 on 11/11/2016.
 */

public class PromotionController {

    private static PromotionController instance = null;
    public static final int PROMOTION_COST = 50;

    private PromotionController() {
    }

    public static PromotionController getInstance() {
        if (instance == null)
            instance = new PromotionController();

        return instance;
    }

    public void getTokens(FeedView feedView) {
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        GetTokensCallback callback = new GetTokensCallback(feedView);
        restClient.getService().getNumTokens(token.getToken(), callback);
    }

    public void promote(String postId, StatisticsView view) {
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PromoteCallback callback = new PromoteCallback(view);
        restClient.getService().promotePost(token.getToken(), postId, callback);
    }

    private static class PromoteCallback implements Callback<JsonObject> {
        private StatisticsView statisticsView;

        PromoteCallback(StatisticsView view){
            statisticsView = view;
        }

        @Override
        public void success(JsonObject result, Response response) {
            SessionDetails.getInstance().numOfTokens -= PROMOTION_COST;
            statisticsView.refreshPromoteButton();
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    }

    private static class GetTokensCallback implements Callback<String> {
        private FeedView mFeed;

        GetTokensCallback(FeedView feed){
            mFeed = feed;
        }

        @Override
        public void success(String numOfTokens, Response response) {
            int code = response.getStatus();
            if (code == 200) {
                Log.d("Chooser", "Get num of tokens");
                int tokens = Integer.parseInt(numOfTokens);
                SessionDetails.getInstance().numOfTokens = tokens;
                mFeed.updateTokens();
            } else {
                Log.e("Chooser", "Error getting num of tokens.");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    }
}
