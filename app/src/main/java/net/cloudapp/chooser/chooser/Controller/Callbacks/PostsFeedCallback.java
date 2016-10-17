package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.util.Log;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.views.FeedView;
import net.cloudapp.chooser.chooser.model.Post;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostsFeedCallback implements Callback<List<Post>> {
    private FeedView mFeed;

    public PostsFeedCallback(FeedView feed){
        mFeed = feed;
    }


    @Override
    public void success(List<Post> postList, Response response) {
        int code = response.getStatus();
        if (code == 200) {
            Log.d("Chooser", "get posts result code is OK.");

            if (postList.size() > 0) {
                Log.d("Chooser", "Number of posts received: " + postList.size());
                PostRepository.postsFeed.addAll(postList);
            }
            else {
                Log.d("Chooser", "Got 0 posts.");
            }
            mFeed.loadPosts();

        } else {
            Log.e("Chooser", "all posts - bad response code.");
        }

        Log.d("Chooser", "Adding " + postList.size() + " Posts to repository");

    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }
}
