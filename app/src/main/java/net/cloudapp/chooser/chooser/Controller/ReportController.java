package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.ReportCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.FeedView;

/**
 * Created by Ben on 28/10/2016.
 */
public class ReportController {
    private FeedView feed;

    public ReportController (FeedView feed) {
        this.feed = feed;
    }
    public void report(String postId){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        restClient.getService().report(token.getToken(), postId, new ReportCallback(feed));
    }
}
