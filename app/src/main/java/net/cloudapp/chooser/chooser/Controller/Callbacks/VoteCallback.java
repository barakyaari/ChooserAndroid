package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.util.Log;

import net.cloudapp.chooser.chooser.views.FeedView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VoteCallback implements Callback<Void> {

    private FeedView feed;

    public VoteCallback(FeedView feed){
        this.feed = feed;
    }

    @Override
    public void success(Void aVoid, Response response) {
        Log.i("Chooser", "Vote callback received");
        feed.refreshFeedRepository();
    }

    @Override
    public void failure(RetrofitError error) {

    }
}
