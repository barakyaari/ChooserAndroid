package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PostsFeedCallback;
import net.cloudapp.chooser.chooser.Controller.Callbacks.VoteCallback;
import net.cloudapp.chooser.chooser.views.Feed;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;

public class PostsController {
    private Feed mFeed;

    public PostsController(Feed feed){
        mFeed = feed;
    }

    public void getPosts(){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PostsFeedCallback callback = new PostsFeedCallback(mFeed);
        restClient.getService().allPosts(token.getToken(), callback);
    }
}
