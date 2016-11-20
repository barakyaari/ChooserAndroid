package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.VoteCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.FeedView;

public class VoteController {
    private FeedView feed;

    public VoteController (FeedView feed) {
        this.feed = feed;
    }
    public void vote(String postId, int selected){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        restClient.getService().vote(token.getToken(), postId, selected, new VoteCallback(feed));
    }
}
