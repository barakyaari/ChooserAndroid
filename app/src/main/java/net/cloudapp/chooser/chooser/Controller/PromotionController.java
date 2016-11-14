package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PromoteCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsView;


public class PromotionController {
    public void promote(String postId, StatisticsView view) {
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PromoteCallback callback = new PromoteCallback(view);
        restClient.getService().promotePost(token.getToken(), postId, callback);
    }
}
