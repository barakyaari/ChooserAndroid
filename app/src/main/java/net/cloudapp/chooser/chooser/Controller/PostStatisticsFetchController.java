package net.cloudapp.chooser.chooser.Controller;

import android.util.Log;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PostStatisticsCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.model.PostStatistics;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments.StatisticsFragmentManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ben on 19/10/2016.
 */
public class PostStatisticsFetchController {

    private StatisticsFragmentManager mStatisticsFragmentManager;
    private String post_id;

    public PostStatisticsFetchController(StatisticsFragmentManager sfm, String post_id){
        this.mStatisticsFragmentManager = sfm;
        this.post_id = post_id;
    }

    public void getPostStatistics() {
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PostStatisticsCallback callback = new PostStatisticsCallback(mStatisticsFragmentManager);
        restClient.getService().getStatistics(token.getToken(), post_id, callback);
        Log.d("Chooser", "Got post statistics");
    }
}
