package net.cloudapp.chooser.chooser.Controller;

import android.content.Context;

import com.facebook.AccessToken;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PromoteCallback;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;


public abstract class PromotionController {
    public void promote(String postId, Context context) {
        AccessToken token = SessionDetails.getInstance().getAccessToken();
        RestClient restClient = new RestClient();
        PromoteCallback callback = new PromoteCallback(context) {
            @Override
            public void callInSuccess() {
                doInSuccess();
            }
        };
        restClient.getService().promotePost(token.getToken(), postId, callback);
    }
    public abstract void doInSuccess();
}
