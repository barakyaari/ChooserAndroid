package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PostsFeedCallback;
import net.cloudapp.chooser.chooser.views.FeedView;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.MyPosts.MyPostsView;

public class MyPostsFetchController {
    private FeedView mFeed;
    private MyPostsView myPosts;

    public MyPostsFetchController(FeedView feed){
        mFeed = feed;
    }

    public MyPostsFetchController(MyPostsView myPosts){
        this.myPosts = myPosts;
    }

    public void getPosts(){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PostsFeedCallback callback = new PostsFeedCallback(mFeed);
        restClient.getService().allPosts(token.getToken(), callback);
    }

    public void getMyPosts(){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PostsFeedCallback callback = new PostsFeedCallback(myPosts);
        restClient.getService().getMyPosts(token.getToken(), callback);
    }
}
