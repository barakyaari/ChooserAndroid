package net.cloudapp.chooser.chooser.Controller;

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

    private StatisticsFragmentManager sfm;
    private String post_id;

    public PostStatisticsFetchController(StatisticsFragmentManager sfm, String post_id){
        this.sfm = sfm;
        this.post_id = post_id;
    }

    public void getPostStatistics(){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PostStatisticsCallback callback = new PostStatisticsCallback(sfm);
        restClient.getService().getStatistics(token.getToken(), post_id, callback);
    }


    public void getTempPostStatistics() {
        PostStatistics postStatistics = new PostStatistics();
        postStatistics.femaleVotes1 = 53;
        postStatistics.femaleVotes2 = 37;

        postStatistics.maleVotes1 = 39;
        postStatistics.maleVotes2 = 48;
        //Vote1 = 96
        //Vote2 = 81
        Integer[] array = {0,0,0,0,0,0,0,0,0,0,0,0,5,7,8,2,4,8,10,7,15,10,7,5,0,3,2,0,1,0,2,0};
        Integer[] array2 = {0,0,0,0,0,0,0,0,0,0,0,1,2,5,9,4,2,6,6,5,9,10,11,4,3,1,0,0,2,0,0,0,1};

        ArrayList<Integer> ageVote1 = new ArrayList<>(Arrays.asList(array));
        ArrayList<Integer> ageVote2 = new ArrayList<>(Arrays.asList(array2));

        postStatistics.ageVotes1 = ageVote1;
        postStatistics.ageVotes2 = ageVote2;
        sfm.setPostStatistics(postStatistics);
        sfm.refreshFragments();
    }


}
