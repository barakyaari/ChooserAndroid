package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.util.Log;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.views.FeedView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ben on 14/11/2016.
 */

public class GetTokensCallback implements Callback<String> {
    private FeedView mFeed;

    public GetTokensCallback(FeedView feed){
        mFeed = feed;
    }

    @Override
    public void success(String numOfTokens, Response response) {
        int code = response.getStatus();
        if (code == 200) {
            Log.d("Chooser", "Get num of tokens");
            SessionDetails.getInstance().numOfTokens = Integer.parseInt(numOfTokens);
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
