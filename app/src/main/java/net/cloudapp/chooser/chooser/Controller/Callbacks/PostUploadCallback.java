package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import net.cloudapp.chooser.chooser.Controller.PromotionController;
import net.cloudapp.chooser.chooser.views.AddPostView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by t-baya on 10/4/2016.
 */

public class PostUploadCallback implements Callback<String> {
    private AddPostView mContext;

    public PostUploadCallback(Context context){

        mContext = (AddPostView) context;
    }
    @Override
    public void success(String postid, Response response) {
        Toast.makeText(mContext, "Post uploaded!", Toast.LENGTH_SHORT).show();
        Log.d("Chooser", "Post uploaded!");
        if (mContext.promote && response.getStatus() == 200) {
            PromotionController promotionController = new PromotionController() {
                @Override
                public void doInSuccess() {}
            };
            promotionController.promote(postid, mContext);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("Chooser", "Post upload failed");
        error.printStackTrace();
    }
}
