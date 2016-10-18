package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.widget.Toast;

import net.cloudapp.chooser.chooser.Controller.MyPostsFetchController;
import net.cloudapp.chooser.chooser.views.MyPosts.MyPostsView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeletePostCallback implements Callback<Void> {
    MyPostsView view;
    public DeletePostCallback (MyPostsView view) {
        this.view = view;
    }

    @Override
    public void success(Void aVoid, Response response) {
        if (response.getStatus() == 200) {
            Toast.makeText(view, "Post Deleted!", Toast.LENGTH_LONG).show();
            MyPostsFetchController myPostsFetchController = new MyPostsFetchController(view);
            myPostsFetchController.getMyPosts();
        }
    }

    @Override
    public void failure(RetrofitError error) {

     }
}
