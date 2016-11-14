package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.GetTokensCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.views.FeedView;

/**
 * Created by Ben on 14/11/2016.
 */

public class GetTokensController {
    public void getTokens(FeedView feedView) {
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        GetTokensCallback callback = new GetTokensCallback(feedView);
        restClient.getService().getNumTokens(token.getToken(), callback);
    }
}
