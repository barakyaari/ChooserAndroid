package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.DeletePostCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsView;

/**
 * Created by Ben on 18/10/2016.
 */
public class DeletePostController {
    public void deletePost(String postId, StatisticsView view){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        restClient.getService().deletePost(token.getToken(), postId, new DeletePostCallback(view));
    }
}

