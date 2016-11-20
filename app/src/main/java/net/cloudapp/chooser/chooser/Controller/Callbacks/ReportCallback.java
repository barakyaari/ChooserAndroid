package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.util.Log;

import net.cloudapp.chooser.chooser.views.FeedView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ben on 28/10/2016.
 */
public class ReportCallback implements Callback<Void> {

    private FeedView feed;

    public ReportCallback(FeedView feed){
        this.feed = feed;
    }

    @Override
    public void success(Void aVoid, Response response) {
        Log.i("Chooser", "Report callback received");
        feed.refreshFeedRepository();
    }

    @Override
    public void failure(RetrofitError error) {

    }
}
