package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.MyPostsFeedCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.MyPosts.MyPostsView;

public class MyPostsFetchController {
    private MyPostsView myPosts;

    public MyPostsFetchController(MyPostsView myPosts){
        this.myPosts = myPosts;
    }

    public void getMyPosts(){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        MyPostsFeedCallback callback = new MyPostsFeedCallback(myPosts);
        restClient.getService().getMyPosts(token.getToken(), callback);
    }
}
