package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.util.Log;

import net.cloudapp.chooser.chooser.model.PostStatistics;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments.StatisticsFragmentManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ben on 19/10/2016.
 */

public class PostStatisticsCallback implements Callback<PostStatistics> {
    private StatisticsFragmentManager sfm;

    public PostStatisticsCallback(StatisticsFragmentManager sfm){
        this.sfm = sfm;
    }


    @Override
    public void success(PostStatistics postStatistics, Response response) {
        int code = response.getStatus();
        if (code == 200) {
            Log.d("Chooser", "get post statistics result code is OK.");

            sfm.setPostStatistics(postStatistics);
            sfm.refreshFragments();

        } else {
            Log.e("Chooser", "PostStatistics - bad response code.");
        }
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }
}

