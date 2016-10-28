package net.cloudapp.chooser.chooser.Controller;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.ReportCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;

/**
 * Created by Ben on 28/10/2016.
 */
public class ReportController {
    public void report(String postId){
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        restClient.getService().report(token.getToken(), postId, new ReportCallback());
    }
}
