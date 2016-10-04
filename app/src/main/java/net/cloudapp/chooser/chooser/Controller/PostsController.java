package net.cloudapp.chooser.chooser.Controller;

import android.content.Context;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PostsFeedCallback;
import net.cloudapp.chooser.chooser.Feed;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;

public class PostsController {
    private Feed mFeed;

    public PostsController(Feed feed){
        mFeed = feed;
    }

    public void getPosts(){
        RestClient restClient = new RestClient();
        PostsFeedCallback callback = new PostsFeedCallback(mFeed);
        restClient.getService().allPosts(SessionDetails.getInstance().getAccessToken().getToken(), callback);
    }
}
