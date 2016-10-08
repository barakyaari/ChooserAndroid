package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.VoteCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;

public class VoteController {
    public void vote(String postId, int selected){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        restClient.getService().vote(token.getToken(), postId, selected, new VoteCallback());
    }
}
