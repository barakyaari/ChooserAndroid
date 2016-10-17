package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PostsFeedCallback;
import net.cloudapp.chooser.chooser.views.FeedView;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;

public class PostsFetchController {
    private FeedView mFeed;

    public PostsFetchController(FeedView feed){
        mFeed = feed;
    }

    public void getPosts(){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PostsFeedCallback callback = new PostsFeedCallback(mFeed);
        restClient.getService().allPosts(token.getToken(), callback);
    }
}
